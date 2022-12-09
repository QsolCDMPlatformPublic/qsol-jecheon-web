package qsol.qsoljecheonweb.error.diagnosis;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class DisconeectionException extends QsolRuntimeException {

	public DisconeectionException() {
		super("다른 관리자와 연결된 진단기입니다.\n연결을 끊을 수 없습니다.");
	}
}
