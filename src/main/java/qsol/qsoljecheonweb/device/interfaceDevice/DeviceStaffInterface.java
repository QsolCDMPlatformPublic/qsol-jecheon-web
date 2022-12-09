package qsol.qsoljecheonweb.device.interfaceDevice;

import java.time.LocalDateTime;

public interface DeviceStaffInterface {

    // gw_regist_info
    Long getLocid();
    String getMacAddress();

    // gw_status_info
    String getStatus();// DB에는 숫자값 자료형이지만, SQL에서 CASE 구분으로 문자열로 받아올 것임
    String getManagerId();

    // etc
    String getAction();
}
