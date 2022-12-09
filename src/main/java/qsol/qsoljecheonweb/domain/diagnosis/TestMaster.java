package qsol.qsoljecheonweb.domain.diagnosis;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_test_master")
@Getter
@Setter
@DynamicUpdate
public class TestMaster extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_id")
    private int testid; // 테스트 마스터 테이블 id

    @Column(name = "req_id") // 진단 id
    private int reqid;

    @Column(name = "loc_id") // 진단기(단말기) id
    private int locid;

    @Column(name = "customer_id") // 고객 id, 0이면 Guest
    private String customerId;

    @Column(name = "evccid") // EVCCID
    private String evccid;

    @Column(name = "start_dt") // 진단 시작 일시
    private LocalDateTime startDt;

    @Column(name = "end_dt") // 진단 종료 일시
    private LocalDateTime endDt;

    @Column(name = "last_soc") // 최종 SOC
    private BigDecimal lastSoc;

    @Column(name = "soh") // SOH
    private BigDecimal soh;

    @Column(name = "success_yn") // 진단 시작 일시
    private Long successYn;

    @Column(name = "result_msg") // 결과 메시지
    private String resultMsg;

    @Column(name = "manager_id") // 관리자 아이디
    private String managerId;
}