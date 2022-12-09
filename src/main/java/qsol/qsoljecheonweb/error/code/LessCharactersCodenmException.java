package qsol.qsoljecheonweb.error.code;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class LessCharactersCodenmException extends QsolRuntimeException {

    public LessCharactersCodenmException() {
        super("코드 이름은 최소 두 글자 이상이어야 합니다.");
    }
}