package qsol.qsoljecheonweb.customers.interfaceCustomer;

import java.time.LocalDateTime;

public interface CustomersInterface {
    // tb_customers
    Long getCid();
    String getCustomersName();
    String getCustomersPhone();
    String getCustomersAddress1();
    String getCustomersAddress2();
    String getCustomersZipcode();
    String getCustomersCountry();
    LocalDateTime getCustomersRegistDt();

    String getCustomersShowAddress();
    String getCustomersVehicleCount();

    // tb_customer_car
    Long getCarId();
    Long getCustomerId();
    String getCustomersCarNumber();
    String getCustomersCarMaker();
    String getCustomersCarName();
    String getCustomersCarYear();
    Long getCustomersCarMileage();
    //LocalDateTime getRegistDt();

    // etc
    Long getTestCount();
    String getVehicleMaker();
    String getVehicleName();
    //Long getCustomersTestResultCount();
}
