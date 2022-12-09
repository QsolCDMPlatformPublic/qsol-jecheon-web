package qsol.qsoljecheonweb.error.code;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ReferenceNullException extends QsolRuntimeException {

	public ReferenceNullException() {
		super("차량 제조사를 선택해주세요.");
	}
}
