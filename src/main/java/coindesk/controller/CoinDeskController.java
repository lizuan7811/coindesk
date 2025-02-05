package coindesk.controller;

import coindesk.model.CoinDeskApiRequest;
import coindesk.service.CoindeskApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
* @Description: 提供幣別 CRUD controller
* @date: 2025/2/4
* @time: 下午 09:47
**/
@RequestMapping("/coindesk")
@RestController
public class CoinDeskController {

    private final CoindeskApiService coindeskApiService;

    @Autowired
    public CoinDeskController(CoindeskApiService coindeskApiService) {
        this.coindeskApiService = coindeskApiService;
    }

    @GetMapping("/{code}")
    public String query(@PathVariable String code) {
        return coindeskApiService.query(code);
    }

    @PostMapping("/")
    public void create(@RequestBody CoinDeskApiRequest coinDeskApiRequest){
        coindeskApiService.create(coinDeskApiRequest);
    }

    @PutMapping("/")
    public String modify(@RequestBody CoinDeskApiRequest coinDeskApiRequest){
        return coindeskApiService.modify(coinDeskApiRequest);
    }

    @DeleteMapping("/{code}")
    public void delete(@PathVariable String code){
        coindeskApiService.delete(code);
    }

    @GetMapping("/showdata")
    public String coindeskShowDataApi(){
        return coindeskApiService.callCoindeskApi();
    }

    @GetMapping("/transdata")
    public String coindeskTransDataApi(){
        return coindeskApiService.transCoindeskApi();
    }

}
