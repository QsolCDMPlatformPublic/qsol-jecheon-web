package qsol.qsoljecheonweb.common.model.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class ListDto<T> {
	private Long count;
	private List<T> list;
	private String nowDate;
	private List<T> topShowData;

	
	public ListDto() {
		this.count = 0L;
		this.list = new ArrayList<>();
		this.nowDate = "";
		this.topShowData = new ArrayList<>();
	}
}
