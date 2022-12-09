package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

@SuppressWarnings("serial")
public class NotMatchedLoginInfomationException extends QsolRuntimeException {

	public NotMatchedLoginInfomationException() {
		super("입력한 로그인 정보가 올바르지 않습니다."); // login.error.mismatch
	}
}
