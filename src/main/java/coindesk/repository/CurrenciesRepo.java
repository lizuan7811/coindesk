package coindesk.repository;

import coindesk.entity.Currencies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @description: Currencies Repository
 * @author: Lizuan
 * @date: 2024/1/22
 * @time: 下午 10:44
 **/
@Repository
public interface CurrenciesRepo extends JpaRepository<Currencies,Long> {
    Optional<Currencies> findByCurrencyCode(String currencyCode);
}
