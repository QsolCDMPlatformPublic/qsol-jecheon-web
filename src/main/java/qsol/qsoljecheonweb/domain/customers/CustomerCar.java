package qsol.qsoljecheonweb.domain.customers;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;

@Entity
@Table(name = "tb_customer_car")
@Getter
@Setter
@DynamicUpdate
public class CustomerCar extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "vehicle_no")
    private String customersCarNumber;

    @Column(name = "var_cd")
    private String customersCarMaker;

    @Column(name = "vml_cd")
    private String customersCarName;

    @Column(name = "car_year")
    private String customersCarYear;

    @Column(name = "mileage")
    private String customersCarMileage;

}
