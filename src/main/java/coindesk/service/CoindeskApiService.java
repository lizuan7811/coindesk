package coindesk.service;

import coindesk.model.CoinDeskApiRequest;

/**
 * @description: TODO
 * @author: Lizuan
 * @date: 2025/2/4
 * @time: 下午 10:03
 **/
public interface CoindeskApiService {
    String query(String code);
    void create(CoinDeskApiRequest coinDeskApiRequest);
    String modify(CoinDeskApiRequest coinDeskApiRequest);
    void delete(String code);
    String callCoindeskApi();

    String transCoindeskApi();

}
