package qsol.qsoljecheonweb.error.code;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class CodeDuplicateException extends QsolRuntimeException {

	public CodeDuplicateException() {
		super("이미 존재하는 코드입니다.");
	}
}
