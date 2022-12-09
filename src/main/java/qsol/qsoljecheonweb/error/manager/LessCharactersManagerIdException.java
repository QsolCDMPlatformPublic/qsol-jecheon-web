package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class LessCharactersManagerIdException extends QsolRuntimeException {

    public LessCharactersManagerIdException() {
        super("아이디가 4글자보다 작습니다.");
    }
}