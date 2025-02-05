package coindesk.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

/**
 * @description: CoinDesk Request
 * @author: Lizuan
 * @date: 2025/2/4
 * @time: 下午 11:27
 **/
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinDeskApiRequest {
    @JsonProperty("currencyCode")
    private String currencyCode;

    @JsonProperty("currencyName")
    private String currencyName;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("exchangeRate")
    private String exchangeRate;
}