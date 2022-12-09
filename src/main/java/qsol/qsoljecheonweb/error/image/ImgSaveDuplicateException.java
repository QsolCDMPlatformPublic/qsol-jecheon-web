package qsol.qsoljecheonweb.error.image;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ImgSaveDuplicateException extends QsolRuntimeException {

	public ImgSaveDuplicateException() {
		super("제조사 및 모델에 해당하는 차량 이미지가 이미 존재합니다.\n신규 등록을 원하시면 기존의 이미지를 삭제하고 다시 시도해주세요.");
	}
}
