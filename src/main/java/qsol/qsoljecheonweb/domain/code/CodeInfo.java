package qsol.qsoljecheonweb.domain.code;

import lombok.Getter;
import lombok.Setter;
import qsol.qsoljecheonweb.common.model.Useyn;

import javax.persistence.*;

@Entity
@Table(name="tb_codeinfo")
@Setter
@Getter
@IdClass(CodeInfoId.class)   //코드테이블의 codegp, code가 primark key로 정의
public class CodeInfo {
    @Id
    @Column(name="code")
    private String code;

    @Id
    @Column(name="codegp")
    private String codegp;

    @Column(name="codenm")
    private String codenm;

    @Column(name="reference")
    private String reference;

    @Column(name="useyn")
    private Boolean useyn;
}


