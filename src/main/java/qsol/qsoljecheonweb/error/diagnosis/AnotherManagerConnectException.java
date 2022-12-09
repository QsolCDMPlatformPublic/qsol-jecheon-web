package qsol.qsoljecheonweb.error.diagnosis;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class AnotherManagerConnectException extends QsolRuntimeException {

	public AnotherManagerConnectException() {
		super("다른 관리자와 이미 연결된 진단기입니다.\n다른 진단기를 선택해주세요.");
	}
}
