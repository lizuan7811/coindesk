package coindesk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 轉換後資料
 * @author: Lizuan
 * @date: 2025/2/4
 * @time: 下午 11:05
 **/
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransformedApiData {
    private String updateTime;
    private List<CurrencyDetails> currencies;
}