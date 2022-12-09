package qsol.qsoljecheonweb.domain.device;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;

@Entity
@Table(name = "tb_gw_regist_info")
@Getter
@Setter
@DynamicUpdate
public class GwRegistInfo extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loc_id")
    private Long locid; // 진단기 ID

    @Column(name = "mas_id") // 진단기(단말기) 마스터 ID
    private Long masid;

    @Column(name = "firm_ver") // 펌웨어 버전
    private String firmver;

    @Column(name = "ip_addr") // IP 주소
    private String ipAddress;

    @Column(name = "mac_addr") // 제품 MAC 주소
    private String macAddress;

    /*@Column(name = "log_type") // 로그 타입
    private Long logType;
*/
    @Column(name = "latitude") // 위도
    private String latitude;

    @Column(name = "longitude") // 경도
    private String longitude;

    @Column(name = "detail_desc") // 상세 지역 설명
    private String detail;
}
