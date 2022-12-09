package qsol.qsoljecheonweb.error.diagnosis;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class AccessPermissionException extends QsolRuntimeException {

	public AccessPermissionException(String manager) {
		super("연결 권한이 없습니다.\n현재 충전 진행중인 관리자 : [" + manager + "]");
	}
}
