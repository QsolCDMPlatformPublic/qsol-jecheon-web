package qsol.qsoljecheonweb.customers.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
public class CustomerCarViewDto extends CustomersViewDto {
    private Long carId;
    //@NotNull(message= "{customers.customerId.error.required}")
    private Long customerId;
    private String customersCarNumber;
    private String customersCarMaker;
    private String customersCarName;
    private String customersCarYear;
    private Long customersCarMileage;

    public Boolean isLessCharacters(String charactors) {
        return charactors.length() < 4;
    }
}
