package qsol.qsoljecheonweb.error.diagnosis;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class AllreadyToMeException extends QsolRuntimeException {

	public AllreadyToMeException() {
		super("이미 나와 연결된 진단기 입니다.");
	}
}
