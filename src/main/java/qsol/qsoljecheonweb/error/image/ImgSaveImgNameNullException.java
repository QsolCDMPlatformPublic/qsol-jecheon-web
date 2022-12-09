package qsol.qsoljecheonweb.error.image;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class ImgSaveImgNameNullException extends QsolRuntimeException {

	public ImgSaveImgNameNullException() {
		super("이미지가 등록되지 않았습니다.");
	}
}
