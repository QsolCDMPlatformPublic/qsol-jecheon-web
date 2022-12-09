package qsol.qsoljecheonweb.result.interfaceResult;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ResultInterface {
    // tb_test_result
    int getTestid();
    BigDecimal getSoh();
    LocalDateTime getRegistDt();

    // tb_test_car
    String getCarNumber();
    String getCarMaker();
    String getCarName();

    // tb_test_master
    String getCustomerId();
    String getMacAddress();

    // tb_customers
    String getCustomerName();


}
