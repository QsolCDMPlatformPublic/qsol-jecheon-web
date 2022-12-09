package qsol.qsoljecheonweb.diagnosis.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSaveInfo {
    private Long locid;
    private Long cid;
    private String customerName;
    private String vehicleNo;
    private String varcd;
    private String vmlcd;
    private String varCodeNm;
    private String vmlCodeNm;
    private String carYear;
    private Long mileage;
    private int testid;


}
