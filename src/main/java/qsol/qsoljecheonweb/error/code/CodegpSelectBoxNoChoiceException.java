package qsol.qsoljecheonweb.error.code;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class CodegpSelectBoxNoChoiceException extends QsolRuntimeException {

	public CodegpSelectBoxNoChoiceException() {
		super("코드 그룹을 선택해주세요.");
	}
}
