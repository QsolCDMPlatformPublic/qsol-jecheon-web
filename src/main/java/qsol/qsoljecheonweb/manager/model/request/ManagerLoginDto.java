package qsol.qsoljecheonweb.manager.model.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManagerLoginDto {
	
	@NotBlank(message="Please enter your ID.") //{manager.userId.error.required}
	private String managerId;
	
	@NotBlank(message="Please enter your Password.") // {manager.password.error.required}
	private String managerPw;

	private Boolean saveLoginInfo;

}
