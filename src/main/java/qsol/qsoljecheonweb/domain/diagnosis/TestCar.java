package qsol.qsoljecheonweb.domain.diagnosis;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_test_car")
@Getter
@Setter
@DynamicUpdate
public class TestCar extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_car_id")
    private Long testCarId; // 테스트 차량 테이블 id

    @Column(name = "test_id") // 테스트 마스터 테이블 id
    private Long testid;

    @Column(name = "vehicle_no") // 차량 번호
    private String vehicleNo;

    @Column(name = "var_cd") // 차량 제조사
    private String varcd;

    @Column(name = "vml_cd") // 차량 모델
    private String vmlcd;

    @Column(name = "car_year") // 차량 연식
    private BigDecimal carYear;

    @Column(name = "mileage") //  주행 거리
    private BigDecimal mileage;

}