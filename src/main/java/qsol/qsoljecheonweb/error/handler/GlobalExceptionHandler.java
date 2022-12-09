package qsol.qsoljecheonweb.error.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import qsol.qsoljecheonweb.error.QsolRuntimeException;
import qsol.qsoljecheonweb.error.response.ErrorInfo;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@ControllerAdvice
@RestController
@Slf4j
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;

	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorInfo handleException(final MethodArgumentNotValidException e, Locale locale) {
		log.error("---- handleException MethodArgumentNotValidException --- {}, {}", locale.getDisplayLanguage(), locale.getDisplayCountry());

		final ErrorInfo errorInfo = new ErrorInfo();
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorInfo.setErrorMessages(new ArrayList<>());

		e.getBindingResult().getFieldErrors().stream().forEach(fieldError -> {
			log.info("{}, {}, {}", fieldError.getField(), fieldError.getDefaultMessage());
			errorInfo.getErrorMessages().add(fieldError.getDefaultMessage());
		});

		return errorInfo;
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	@ExceptionHandler(HttpClientErrorException.Forbidden.class)
	public HttpClientErrorException.Forbidden forbiddenHandleException(HttpClientErrorException.Forbidden e) {
		log.error("--- FORBIDDEN --- ", e);
		return e;
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(HttpClientErrorException.Unauthorized.class)
	public ResponseEntity<ErrorInfo> unauthorizedHandleException(HttpSession session, HttpClientErrorException.Unauthorized e, Locale locale) {

		log.error("--- UNATHORIZED --- ", e);
		return ResponseEntity.status(e.getStatusCode()).body(new ErrorInfo (e.getStatusCode().value(), messageSource.getMessage("login.error.mismatch", null, locale), null));
	}

	@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
	@ExceptionHandler(HttpClientErrorException.TooManyRequests.class)
	public ResponseEntity<ErrorInfo> lockedHandleException(HttpClientErrorException e, Locale locale) {

		log.error("---TOO_MANY_REQUESTS --- ", e);
		return ResponseEntity.status(e.getStatusCode()).body(new ErrorInfo (e.getStatusCode().value(), messageSource.getMessage("login.error.locked", null, locale), null));
	}

	@ExceptionHandler(value=Exception.class)
	public ResponseEntity<ErrorInfo> handleException(Exception e) {
		log.error("--- global exception handler ---", e);
		//e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorInfo (999, "Error Occurs", null));
	}

	@ResponseBody
	@ExceptionHandler(QsolRuntimeException.class) // 만들어진 오류 클래스로부터 QsolRuntimeException을 거쳐 이곳으로 오게됨
	public ResponseEntity<ErrorInfo> handleException (final QsolRuntimeException e) {
		log.error("--- handleException handler --- {}", e.getLocalizedMessage());
		List<String> errorMessage = new ArrayList<>();
		errorMessage.add(e.getLocalizedMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
				body(new ErrorInfo(998, "", errorMessage));
	}

}