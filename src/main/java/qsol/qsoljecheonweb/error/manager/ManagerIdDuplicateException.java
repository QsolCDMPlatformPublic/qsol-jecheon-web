package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ManagerIdDuplicateException extends QsolRuntimeException {

	public ManagerIdDuplicateException() {
		super("이미 존재하는 아이디입니다.");
	}
}
