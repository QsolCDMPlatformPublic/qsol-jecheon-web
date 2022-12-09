package qsol.qsoljecheonweb.diagnosis.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SendDataViewDto {

   private Long locid;
   private Long customerId;
   private String customerName;
   private String vehicleNo;
   private String varcd;
   private String vmlcd;
   private String varCodeNm;
   private String vmlCodeNm;
   private String carYear;
   private Long mileage;
   private String managerId;
}
