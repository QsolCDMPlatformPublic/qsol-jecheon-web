package qsol.qsoljecheonweb.error.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorInfo {
	private Integer errorCode;
	private String errorMessage;
	private List<String> errorMessages;
}
