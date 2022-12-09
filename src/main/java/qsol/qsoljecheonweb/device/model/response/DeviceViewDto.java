package qsol.qsoljecheonweb.device.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class DeviceViewDto {
    // gw_regist_info
    private Long locid;
    private String firmver;
    private String ipAddress;
    private String macAddress;
    private String detail;
    private String detailAbb;
    private LocalDateTime registDt;

    // gw_status_info
    private String status; // DB에는 숫자값 자료형이지만, SQL에서 CASE 구분으로 문자열로 받아올 것임
    private String managerId;

    // etc
    private String action;
}
