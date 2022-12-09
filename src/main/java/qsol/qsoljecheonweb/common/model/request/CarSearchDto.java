package qsol.qsoljecheonweb.common.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarSearchDto {
	private Integer pageNo;
	private Integer countPerPage = 5;
}
