package qsol.qsoljecheonweb.manager.model.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;


@Getter
@Setter
@ToString
public class ManagerViewDto {
    private String managerId;
    private String managerPw;
    private String managerNm;
    private String managerPhone;
    private String managerTel;
    private String managerEmail;
    private String usingStatus;

    private String confirmPw;

    public Boolean isLessCharacters(String charactors) {
        return charactors.length() < 4;
    }
}
