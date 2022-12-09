package qsol.qsoljecheonweb.domain.customers;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;

@Entity
@Table(name = "tb_customers")
@Getter
@Setter
@DynamicUpdate
public class Customers extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String customersName;

    @Column(name = "phone")
    private String customersPhone;

    @Column(name = "address1")
    private String customersAddress1;

    @Column(name = "address2")
    private String customersAddress2;

    @Column(name = "zipcode")
    private String customersZipcode;

    @Column(name = "country")
    private String customersCountry;
}
