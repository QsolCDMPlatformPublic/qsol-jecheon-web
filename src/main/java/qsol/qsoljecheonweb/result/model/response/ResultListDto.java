package qsol.qsoljecheonweb.result.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class ResultListDto {
    // tb_test_result
    private int testid;
    private BigDecimal soh;
    private LocalDateTime registDt;

    // tb_test_car
    private String carNumber;
    private String carMaker;
    private String carName;

    // tb_test_master
    private String customerId;
    private String macAddress;

    // tb_customers
    private String customerName;


}
