package qsol.qsoljecheonweb.customers.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
public class CustomersViewDto {
    private Long cid;
    private String customersName;
    private String customersPhone;
    private String customersAddress1;
    private String customersAddress2;
    private String customersZipcode;
    private String customersCountry;

    private LocalDateTime customersRegistDt;

    private String customersShowAddress; // 화면에 보여줄 고객 주소 정보 (Zipcode + Address1 + Address2 + Country)
    //private String customersVehicleCount; // 고객의 등록된 차량 수
    //private Long customersTestResultCount; // 고객의 진단 결과 수

    private Long testCount; // 해당 차량의 진단 횟수
    private String vehicleMaker;
    private String vehicleName;

    public Boolean isLessCharacters(String charactors) {
        return charactors.length() < 4;
    }
}
