package qsol.qsoljecheonweb.domain.code;

import lombok.Getter;
import lombok.Setter;
import qsol.qsoljecheonweb.common.model.Useyn;

import javax.persistence.*;

@Entity
@Table(name="tb_codegroup")
@Setter
@Getter
public class CodeGroup {
    @Id
    @Column(name="codegp")
    private String codegp;

    @Column(name="codegpnm")
    private String codegpnm;

    @Column(name="useyn")
    @Enumerated(EnumType.ORDINAL)
    private Useyn useyn;
}
