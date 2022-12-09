package qsol.qsoljecheonweb.domain.code;

import java.io.Serializable;

//CodeInfo 테이블의 CODEGP, CODE Primary Key 필드 정의
public class CodeInfoId implements Serializable {
    private String code;  //코드테이블 code Primary Key
    private String codegp; //코드테이블 codeGP Primary Key

    protected CodeInfoId() { }  //기본 생성자 추가

    public CodeInfoId(String codegp, String code) {
        this.codegp = codegp;
        this.code = code;
    }

}
