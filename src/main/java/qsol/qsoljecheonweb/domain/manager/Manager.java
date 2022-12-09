package qsol.qsoljecheonweb.domain.manager;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.lang.Nullable;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithRegistDtWithoutId;
import qsol.qsoljecheonweb.domain.etc.BaseEntityWithUpdatedWithoutId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_manager")
@Getter
@Setter
public class Manager extends BaseEntityWithRegistDtWithoutId {
    @Id
    @Column(name = "manager_id")
    private String managerId;

    @Column(name = "manager_pw")
    private String managerPw;

    @Column(name = "manager_nm")
    private String managerNm;

    @Column(name = "manager_phone")
    private String managerPhone;

    @Column(name = "manager_tel")
    private String managerTel;

    @Column(name = "manager_email")
    private String managerEmail;

    /*@Column(name = "using")
    private Boolean using;*/

}
