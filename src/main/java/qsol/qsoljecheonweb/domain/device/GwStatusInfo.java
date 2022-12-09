package qsol.qsoljecheonweb.domain.device;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;

@Entity
@Table(name = "tb_gw_status_info")
@Getter
@Setter
@DynamicUpdate
public class GwStatusInfo extends BaseEntityWithRegistDtWithoutId {
/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
*/

    @Id
    @Column(name = "loc_id") // 진단기(단말기) 마스터 ID
    private Long locid;

    @Column(name = "manager_id") // 펌웨어 버전
    private String managerId;

    @Column(name = "status_cd") // IP 주소
    private Long status;

    @Column(name = "connect")
    private Long connect;

}
