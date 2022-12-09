package qsol.qsoljecheonweb.error.manager;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class LessCharactersManagerPwException extends QsolRuntimeException {

    public LessCharactersManagerPwException() {
        super("비밀번호 또는 비밀번호 확인이 4글자보다 작습니다.");
    }
}