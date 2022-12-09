package qsol.qsoljecheonweb.domain.diagnosis;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_test_result")
@Getter
@Setter
@DynamicUpdate
public class TestResult extends BaseEntityWithRegistDtWithoutId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_result_id")
    private Long testResultId; // 테스트 결과 테이블 id

    @Column(name = "test_id") // 테스트 마스터 테이블 id
    private Long testid;

    @Column(name = "req_id") // 진단 마스터 id(요청id)
    private Long reqid;

    @Column(name = "elapsed_seconds") // 진단 소요 시간(초)
    private Long elapsedSeconds;

    @Column(name = "grade") // 등급 S A B C D
    private String grade;

    @Column(name = "last_soc") // 계산 SOC
    private BigDecimal lastSoc;

    @Column(name = "soh") // 계산 SOH
    private BigDecimal soh;

    @Column(name = "reason_msg") // 처리 불가 사유 메시지
    private String reasonMsg;

}