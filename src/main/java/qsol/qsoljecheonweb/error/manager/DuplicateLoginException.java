package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class DuplicateLoginException extends QsolRuntimeException {

	public DuplicateLoginException() {
		super("이미 사용중인 아이디입니다.\n로그인 할 수 없습니다.");
	}
}
