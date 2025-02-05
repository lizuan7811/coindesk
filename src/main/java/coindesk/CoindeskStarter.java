package coindesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @description: CoindeskStarter 開始啟動 CoinDesk
 * @time: 2023/12/25 上午 11:47
 **/
@SpringBootApplication
@EnableJpaRepositories("coindesk.*")
@EntityScan(basePackages = "coindesk.*")
@EnableConfigurationProperties
public class CoindeskStarter {

    public static void main(String[] args){
        SpringApplication.run(CoindeskStarter.class,args);
    }

}
