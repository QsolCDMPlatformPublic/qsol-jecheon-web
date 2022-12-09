package qsol.qsoljecheonweb.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.domain.manager.Manager;
import qsol.qsoljecheonweb.error.manager.PasswordBlankException;
import qsol.qsoljecheonweb.error.response.ErrorInfo;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.request.ManagerLoginDto;
import qsol.qsoljecheonweb.manager.model.response.ManagerViewDto;
import qsol.qsoljecheonweb.manager.service.ManagerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/system/manager")
public class ManagerController {

	@Autowired
	private ManagerService managerService;

	@GetMapping() // 화면 우측 상단에 현재 로그인한 manager의 id를 노출하기 위해 세션에서 값을 가져옴
	public ResponseEntity<String> managerIdOfSession(HttpSession session) {
		ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
		return ResponseEntity.ok(managerInfo.getManagerId());
	}

	@GetMapping("/list") // 관리자 목록 테이블 호출 :list
	public ResponseEntity<ListDto<ManagerViewDto>> list(SearchDto searchDto) {
		log.info(" --- manager list called --- {}", searchDto);
		return ResponseEntity.ok(managerService.list(searchDto));
	}

	@PostMapping("/create")
	public ResponseEntity<HttpStatus> create(@RequestBody @Valid ManagerViewDto managerViewDto) throws NoSuchAlgorithmException {
		log.info(" --- manager create called --- {}", managerViewDto);
		managerViewDto.setManagerId(managerViewDto.getManagerId().trim()); // 공백제거
		managerViewDto.setManagerNm(managerViewDto.getManagerNm().trim());
		managerViewDto.setManagerPhone(managerViewDto.getManagerPhone().trim());
		managerViewDto.setManagerTel(managerViewDto.getManagerTel().trim());
		managerViewDto.setManagerEmail(managerViewDto.getManagerEmail().trim());
		if(!managerViewDto.getManagerPw().equals(managerViewDto.getManagerPw().trim()) || !managerViewDto.getConfirmPw().equals(managerViewDto.getConfirmPw().trim())) {
			throw new PasswordBlankException();
		} else {
			managerService.create(managerViewDto);
		}
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{managerId}") // 관리자 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
	public ResponseEntity<ManagerViewDto> view(@PathVariable @Valid String managerId) {
		log.info(" --- manager view called --- {}", managerId);
		return ResponseEntity.ok(managerService.view(managerId));
	}

	@PutMapping()
	public ResponseEntity<HttpStatus> update(@RequestBody @Valid ManagerViewDto managerViewDto) throws NoSuchAlgorithmException {
		log.info(" --- manager update called --- {}", managerViewDto);
		managerViewDto.setManagerNm(managerViewDto.getManagerNm().trim()); // 공백제거
		managerViewDto.setManagerPhone(managerViewDto.getManagerPhone().trim());
		managerViewDto.setManagerTel(managerViewDto.getManagerTel().trim());
		managerViewDto.setManagerEmail(managerViewDto.getManagerEmail().trim());
		managerService.update(managerViewDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/managerDelete/{managerId}")
	public ResponseEntity<HttpStatus> delete(@PathVariable @Valid String managerId) {
		log.info(" --- manager delete called --- {}", managerId);
		managerService.delete(managerId);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/usingStatusChange/{managerId}")
	public void usingStatusChange(@PathVariable @Valid String managerId) {
		log.info(" --- usingStatusChange called --- {}", managerId);
		managerService.usingUpdateFalse(managerId);
	}

}
