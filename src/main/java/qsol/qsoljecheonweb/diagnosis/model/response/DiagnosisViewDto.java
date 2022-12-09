package qsol.qsoljecheonweb.diagnosis.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class DiagnosisViewDto extends BaseEntityWithRegistDtWithoutId {

    // tb_test_result
    private Long testid;
    private Long elapsedSeconds; // 진단 소요 시간(초)
    private String grade; // 등급
    private BigDecimal lastSoc; // 계산 soc
    private BigDecimal soh; // 계산 soh
    private String reasonMsg; // 처리 불가 사유 메시지, 오류 아닌 경우 success

    // tb_test_master
    private LocalDateTime startDt; // 진단 시작 일시
    private LocalDateTime endDt; // 진단 종료 일시
    private String managerId; // 관리자 id

    // tb_test_car
    private String vehicleNo; // 차량번호
    private String carYear; // 차량연식
}
