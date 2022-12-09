package qsol.qsoljecheonweb.error.image;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ImgSaveCodeNullException extends QsolRuntimeException {

	public ImgSaveCodeNullException() {
		super("차량 제조서 또는 차량 모델을 선택하지 않았습니다.");
	}
}
