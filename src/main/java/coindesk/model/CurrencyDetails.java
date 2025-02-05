package coindesk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: TODO
 * @author: Lizuan
 * @date: 2025/2/4
 * @time: 下午 11:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDetails {
    private String currency;
    private String currencyName;
    private double rate;
}