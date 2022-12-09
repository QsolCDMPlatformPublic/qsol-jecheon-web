package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class PasswordBlankException extends QsolRuntimeException {

	public PasswordBlankException() {
		super("비밀번호 및 비밀번호 확인을 입력해주세요.");
	}
}
