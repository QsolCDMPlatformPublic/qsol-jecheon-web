package qsol.qsoljecheonweb.error.customers;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class VehicleDuplicateException extends QsolRuntimeException {

	public VehicleDuplicateException() {
		super("이미 등록된 차량 번호입니다.");
	}
}
