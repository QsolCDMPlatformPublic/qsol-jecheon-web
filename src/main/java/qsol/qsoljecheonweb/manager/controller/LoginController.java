package qsol.qsoljecheonweb.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.device.repository.DeviceStatusRepository;
import qsol.qsoljecheonweb.error.manager.NotMatchedLoginInfomationException;
import qsol.qsoljecheonweb.error.response.ErrorInfo;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.ManagerSaveInfo;
import qsol.qsoljecheonweb.manager.model.request.ManagerLoginDto;
import qsol.qsoljecheonweb.manager.service.ManagerService;
import qsol.qsoljecheonweb.util.QsolMessageDigest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

@Slf4j
@RestController
public class LoginController {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private DeviceStatusRepository deviceStatusRepository;

	@PostMapping("/api/security/login")
	/*<ErrorInfo>*/
	public ResponseEntity<String> login(@RequestBody @Valid ManagerLoginDto managerLoginDto, HttpServletRequest request, HttpSession session) throws NoSuchAlgorithmException {
		System.out.println("g/w-jecheon-web - loginController run");
		log.debug("login:{}", managerLoginDto);
		String password = QsolMessageDigest.getSha512(managerLoginDto.getManagerPw(),
				managerLoginDto.getManagerId().toLowerCase());

		ManagerInfo managerInfo = managerService.login(managerLoginDto);
		if (managerInfo != null) {
			session.setAttribute("manager-info", managerInfo);
			String authorityCheck = managerLoginDto.getManagerId();
			if(managerLoginDto.getSaveLoginInfo() == true) { // 로그인 정보 저장 버튼 클릭 상태로 로그인하면 remember에 true를 저장해놓고 로그아웃에서 사용
				session.setAttribute("remember-id", managerLoginDto.getManagerId());
				session.setAttribute("remember-pw", managerLoginDto.getManagerPw());
			}
			// managerService.usingUpdateTrue(managerInfo.getManagerId()); 관리자로 로그인해서 관리해줄 수 없기에 항상 로그인 가능하도록 주석처리
			ErrorInfo errorInfo = ErrorInfo.builder()
					.errorCode(0)
					.build();
			return ResponseEntity.ok(authorityCheck);
		}
		throw new NotMatchedLoginInfomationException();
	}

	private String getMessage(String key){
		return messageSource.getMessage(key, null, Locale.KOREA);
	}

	@RequestMapping("/api/security/logout")
	public ResponseEntity<HttpStatus> logout(HttpServletRequest request, HttpSession session) {
		ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
		//managerService.usingUpdateFalse(managerInfo.getManagerId()); // 로그아웃시 manager테이블의 using을 0으로 변경
		// 로그아웃되면 현재 진행중이던 진단과 진단기와의 연결을 모두 해제
		//deviceStatusRepository.disconnection(managerInfo.getManagerId()); 제천용은 관리자 아이디가 하나이기에 연결 해제 안함
		if(session.getAttribute("remember-pw") != null) { // 로그인 당시 정보 저장 체크 박스를 체크했다면 세션 소멸 후 아이디와 비밀번호를 다시 SET
			ManagerSaveInfo managerSaveInfo = new ManagerSaveInfo();
			managerSaveInfo.setManagerId((String) session.getAttribute("remember-id"));
			managerSaveInfo.setManagerPw((String) session.getAttribute("remember-pw"));
			session.invalidate();
			session = request.getSession(true); // 소멸된 세션을 다시 생성
			session.setAttribute("save-info", managerSaveInfo);
		} else {
			session.invalidate();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/api/security/loginSaveData")
	public ResponseEntity<Object> loginSaveData(HttpSession session) {
		log.info(" --- loginSaveData called --- ");
		if(session.getAttribute("save-info") != null) {
			return ResponseEntity.ok(session.getAttribute("save-info"));
		} else {
			return ResponseEntity.ok().build();
		}
	}

}
