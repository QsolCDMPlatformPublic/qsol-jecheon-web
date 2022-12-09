package qsol.qsoljecheonweb.error.diagnosis;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class DuplicateConnectException extends QsolRuntimeException {

	public DuplicateConnectException() {
		super("이미 다른 진단기와 연결되어 있습니다.");
	}
}
