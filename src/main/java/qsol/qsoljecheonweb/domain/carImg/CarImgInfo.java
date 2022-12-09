package qsol.qsoljecheonweb.domain.carImg;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;

@Entity
@Table(name = "tb_car_imginfo")
@Getter
@Setter
@DynamicUpdate
public class CarImgInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id; // 테스트 차량 테이블 id

    @Column(name = "img_name") // 차량 이미지 이름
    private String imgName;

    @Column(name = "var_cd") // 차량 제조사
    private String varcd;

    @Column(name = "vml_cd") // 차량 모델
    private String vmlcd;



}