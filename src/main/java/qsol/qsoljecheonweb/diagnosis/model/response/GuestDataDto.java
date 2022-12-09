package qsol.qsoljecheonweb.diagnosis.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GuestDataDto {

    private Long locid;
    //private Long cid = 0L;
    private String vehicleNo;
    private String carMaker;
    private String carName;
    private Long carYear;
    private String carMileage; // 프로시저로 보내기 전에 Long으로 형변환 해서 보내야함

}
