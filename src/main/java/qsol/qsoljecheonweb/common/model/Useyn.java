package qsol.qsoljecheonweb.common.model;

import lombok.Getter;

@Getter
public enum Useyn {
	stopUsing(false, "사용중지"),
	using(true, "사용중");

	private Boolean flag;
	private String message;

	Useyn(Boolean flag, String message) {
		this.flag = flag;
		this.message = message;
	}
}