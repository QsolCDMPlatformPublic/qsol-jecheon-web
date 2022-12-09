package qsol.qsoljecheonweb.common.model.request;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonPatternDto {

    private String telno;  //전화번호 패턴 검사용
    private String email;  //이메일 패턴 검사용
}
