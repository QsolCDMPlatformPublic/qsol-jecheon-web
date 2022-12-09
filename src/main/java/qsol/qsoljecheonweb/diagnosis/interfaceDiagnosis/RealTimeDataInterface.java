package qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface RealTimeDataInterface {

    LocalDateTime getStartDt();
    BigDecimal getSoc();
    Integer getStepCd();

    // String getStatusCd(); // 진단 상태 검사를 tb_test_status_history 에서 tb_doha_status_now로 변경

}
