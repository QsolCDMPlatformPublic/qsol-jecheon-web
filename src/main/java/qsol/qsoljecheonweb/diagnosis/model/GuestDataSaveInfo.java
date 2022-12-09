package qsol.qsoljecheonweb.diagnosis.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GuestDataSaveInfo {
    private Long locid;
    private Long cid;
    private String customerName;
    private String vehicleNo;
    private String carMaker;
    private String carName;
    private String varCodeNm;
    private String vmlCodeNm;
    private Long carYear;
    private String carMileage;
    private int testid;


}
