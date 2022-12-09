package qsol.qsoljecheonweb.error.customers;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class VehicleMakerAndNameException extends QsolRuntimeException {

	public VehicleMakerAndNameException() {
		super("차량 제조사 및 차량 모델을 선택해주세요.");
	}
}
