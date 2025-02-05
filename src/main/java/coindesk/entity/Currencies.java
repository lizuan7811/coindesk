package coindesk.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* @Description: 幣別資訊表
* @date: 2025/2/4
* @time: 下午 09:32
**/
@Table(name="Currencies")
@Entity
@Data
public class Currencies implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @Column(name = "currency_code", nullable = false, length = 10)
    private String currencyCode;

    @Column(name = "ch_name", nullable = false, length = 50)
    private String chName;

    @Column(name = "symbol", length = 10)
    private String symbol;

    @Column(name = "exchange_rate", precision = 18, scale = 6)
    private BigDecimal exchangeRate;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
