package qsol.qsoljecheonweb.error.customers;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class LessCharactersVehicleException extends QsolRuntimeException {

    public LessCharactersVehicleException() {
        super("차량 번호 또는 연식은 4글자 이상이어야 합니다.");
    }
}