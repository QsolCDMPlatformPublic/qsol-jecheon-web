package qsol.qsoljecheonweb.result.interfaceResult;

import java.math.BigDecimal;

public interface ResultViewInterface {
    // tb_test_car
    String getCarNumber();
    String getVarcd();
    String getVmlcd();
    String getCarYear();
    int getMileage();

    // tb_test_result
    String getGrade();
    BigDecimal getSoh();

    // tb_codeinfo
    String getVarCodeNm();
    String getVmlCodeNm();
}
