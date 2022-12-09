package qsol.qsoljecheonweb.customers.model.request;

import lombok.Getter;
import lombok.Setter;
import qsol.qsoljecheonweb.common.model.request.SearchDto;

@Getter
@Setter
public class CustomerSearchDto extends SearchDto {
    private String searchName;
    private String searchMaker;
    private String searchModel;
}
