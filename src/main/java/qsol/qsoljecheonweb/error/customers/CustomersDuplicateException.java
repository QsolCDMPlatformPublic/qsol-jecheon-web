package qsol.qsoljecheonweb.error.customers;

import qsol.qsoljecheonweb.error.QsolRuntimeException;

public class CustomersDuplicateException extends QsolRuntimeException {

	public CustomersDuplicateException() {
		super("이미 등록된 회원입니다.(이름과 휴대폰 중복)");
	}
}
