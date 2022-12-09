package qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface DiagnosisInterface {

    // tb_test_result
    Long getTestid();
    Long getElapsedSeconds();
    String getGrade();
    BigDecimal getLastSoc();
    BigDecimal getSoh();
    String getReasonMsg();

    // tb_test_master
    LocalDateTime getStartDt();
    LocalDateTime getEndDt();
    String getManagerId();

    // tb_test_car
    String getVehicleNo();
    String getCarYear();

}
