import coindesk.CoindeskStarter;
import coindesk.entity.Currencies;
import coindesk.model.CoinDeskApiRequest;
import coindesk.repository.CurrenciesRepo;
import coindesk.service.impl.CoindeskApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = CoindeskStarter.class)
@ExtendWith(MockitoExtension.class)
class CoinDeskTestUnit {

    @MockBean
    private CurrenciesRepo currenciesRepo;
    @Autowired
    private CoindeskApiServiceImpl coindeskApiService;

    private Currencies mockCurrency;

    @BeforeEach
    void setUp() {
        mockCurrency = new Currencies();
        mockCurrency.setCurrencyCode("USD");
        mockCurrency.setSymbol("$");
        mockCurrency.setExchangeRate(new BigDecimal("1.2345"));
        mockCurrency.setChName("美金");
    }

    @Test
    void testQuery_CurrencyFound() {
        // 模擬資料庫中已有的資料
        when(currenciesRepo.findByCurrencyCode("USD")).thenReturn(Optional.of(mockCurrency));
        String result = coindeskApiService.query("USD");
        System.out.println("Coindesk query: " + result);
    }

    @Test
    void testCreate_NewCurrency() {
        CoinDeskApiRequest request = new CoinDeskApiRequest("USD", "美金", "$", "1.2345");
        when(currenciesRepo.findByCurrencyCode("USD")).thenReturn(Optional.empty());
        coindeskApiService.create(request);
        verify(currenciesRepo, times(1)).saveAndFlush(any(Currencies.class));
    }

    @Test
    void testModify_CurrencyExists() {
        // 模擬資料庫中已有的資料
        when(currenciesRepo.findByCurrencyCode("USD")).thenReturn(Optional.of(mockCurrency));
        CoinDeskApiRequest modifyRequest = new CoinDeskApiRequest("USD", "美元", "$", "1.5678");
        String result = coindeskApiService.modify(modifyRequest);
        System.out.println("Coindesk Update: " + result);
        verify(currenciesRepo, times(1)).saveAndFlush(any(Currencies.class));
    }

    @Test
    void testDelete_CurrencyExists() {
        // 模擬資料庫中已有的資料
        when(currenciesRepo.findByCurrencyCode("USD")).thenReturn(Optional.of(mockCurrency));
        coindeskApiService.delete("USD");
        verify(currenciesRepo, times(1)).delete(any(Currencies.class));
    }

    @Test
    void testCallCoindeskApi_Success() {
        String result = coindeskApiService.callCoindeskApi();
        assertNotNull(result);
        System.out.println("Coindesk API Response: " + result);
    }

    @Test
    void testTransCoindeskApi_Success() {
        String result = coindeskApiService.transCoindeskApi();
        assertNotNull(result);
        System.out.println("Coindesk API Transformed Response: " + result);
    }
}
