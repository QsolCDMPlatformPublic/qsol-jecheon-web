package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class NonExistentManagerIdException extends QsolRuntimeException {

	public NonExistentManagerIdException() {
		super("입력한 로그인 정보가 올바르지 않습니다.");
	}
}
