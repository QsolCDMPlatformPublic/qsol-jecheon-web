package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ConfirmPwException extends QsolRuntimeException {

	public ConfirmPwException() {
		super("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
	}
}
