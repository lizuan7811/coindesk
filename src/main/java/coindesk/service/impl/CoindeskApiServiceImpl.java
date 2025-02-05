package coindesk.service.impl;

import coindesk.entity.Currencies;
import coindesk.model.*;
import coindesk.repository.CurrenciesRepo;
import coindesk.service.CoindeskApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @description: 呼叫 coindesk API
 * @author: Lizuan
 * @date: 2025/2/4
 * @time: 下午 10:03
 **/
@Service
@Slf4j
public class CoindeskApiServiceImpl implements CoindeskApiService {
    private static final String COINDESKADDRESS = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final CurrenciesRepo currenciesRepo;

    @Autowired
    public CoindeskApiServiceImpl(ObjectMapper objectMapper, CurrenciesRepo currenciesRepo) {
        this.objectMapper = objectMapper;
        this.currenciesRepo = currenciesRepo;
    }

    @PostConstruct
    private void initHttpClient() {
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);

        httpClient = HttpClients.custom()
                .setConnectionManager(connManager)
                .build();

    }

    @PreDestroy
    public void close() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String query(String code) {
        try {
            Optional<Currencies> currenciesOptional = currenciesRepo.findByCurrencyCode(code);
            return currenciesOptional.map(cur-> {
                        try {
                            return objectMapper.writeValueAsString(cur);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("Failed to serialize currency data", e);
                        }
                    })
                    .orElse("{\"message\":\"Currency not found\"}");
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize currency data", e);
        }
    }


    @Override
    public void create(CoinDeskApiRequest coinDeskApiRequest) {
        if (currenciesRepo.findByCurrencyCode(coinDeskApiRequest.getCurrencyCode()).isPresent()) {
            throw new RuntimeException("Currency already exists: " + coinDeskApiRequest.getCurrencyCode());
        }
        Currencies currencies = new Currencies();
        currencies.setCurrencyCode(coinDeskApiRequest.getCurrencyCode());
        currencies.setSymbol(coinDeskApiRequest.getSymbol());
        currencies.setExchangeRate(new BigDecimal(coinDeskApiRequest.getExchangeRate()));
        currencies.setChName(coinDeskApiRequest.getCurrencyName());
        currencies.setCreatedAt(LocalDateTime.now());
        currenciesRepo.saveAndFlush(currencies);
    }

    @Override
    public String modify(CoinDeskApiRequest coinDeskApiRequest) {
        return currenciesRepo.findByCurrencyCode(coinDeskApiRequest.getCurrencyCode())
                .map(currencies -> {
                    currencies.setChName(coinDeskApiRequest.getCurrencyName());
                    currencies.setExchangeRate(new BigDecimal(coinDeskApiRequest.getExchangeRate()));
                    currencies.setSymbol(coinDeskApiRequest.getSymbol());
                    Currencies modifiedCur = currenciesRepo.saveAndFlush(currencies);
                    try {
                        return objectMapper.writeValueAsString(modifiedCur);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Failed to serialize modified currency", e);
                    }
                })
                .orElse("Currency not found: " + coinDeskApiRequest.getCurrencyCode());
    }

    @Override
    public void delete(String code) {
        Optional<Currencies> currencies = currenciesRepo.findByCurrencyCode(code);
        if (currencies.isPresent()) {
            currenciesRepo.delete(currencies.get());
        } else {
            throw new RuntimeException("Currency not found");
        }
    }

    @Override
    public String callCoindeskApi() {
        try {
            if(httpClient == null) {
                initHttpClient();
            }
            HttpResponse response = httpClient.execute(new HttpGet(COINDESKADDRESS));
            int statusCode = response.getStatusLine().getStatusCode();
            String result = EntityUtils.toString(response.getEntity());

            log.info("Coindesk API Response: {}", result);

            if (statusCode == 200) {
                return result;
            } else {
                log.error("Failed to call Coindesk API, status code: {}", statusCode);
                throw new RuntimeException("Failed to call API, status code: " + statusCode);
            }
        } catch (IOException e) {
            log.error("Error calling Coindesk API", e);
            throw new RuntimeException("Error calling Coindesk API", e);
        }
    }

    @Override
    public String transCoindeskApi() {
        String datas = callCoindeskApi();
        CoinDeskApiResponse response = null;
        try {
            response = objectMapper.readValue(datas, CoinDeskApiResponse.class);

            String updateTime = convertTime(response.getTime().getUpdatedISO());

            List<CurrencyDetails> currencies = new ArrayList<>();
            Map<String, CurrencyInfo> bpi = response.getBpi();

            for (Map.Entry<String, CurrencyInfo> entry : bpi.entrySet()) {
                CurrencyInfo currencyInfo = entry.getValue();
                currencies.add(new CurrencyDetails(
                        entry.getKey(),
                        currencyInfo.getDescription(),
                        currencyInfo.getRate_float()));
            }
            return objectMapper.writeValueAsString(new TransformedApiData(updateTime, currencies));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String convertTime(String time) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(time.trim(), inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            return zonedDateTime.format(outputFormatter);
        } catch (Exception e) {
            log.error("Failed to parse time: {}", time, e);
            return "Invalid time format";
        }
    }
}
