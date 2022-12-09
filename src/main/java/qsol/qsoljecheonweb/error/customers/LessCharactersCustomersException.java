package qsol.qsoljecheonweb.error.customers;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class LessCharactersCustomersException extends QsolRuntimeException {

    public LessCharactersCustomersException() {
        super("고객 이름 또는 휴대폰 번호는 4글자 이상이어야 합니다.");
    }
}