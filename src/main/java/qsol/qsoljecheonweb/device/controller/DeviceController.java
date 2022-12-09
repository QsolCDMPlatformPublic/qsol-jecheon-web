package qsol.qsoljecheonweb.device.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.device.model.response.DeviceStaffViewDto;
import qsol.qsoljecheonweb.device.model.response.DeviceViewDto;
import qsol.qsoljecheonweb.device.service.DeviceService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/system/device")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@GetMapping("/list") // 진단기 목록 테이블 호출 및 검색 :list
	public ResponseEntity<ListDto<DeviceViewDto>> list(SearchDto searchDto) {
		log.info(" --- device list called --- {}", searchDto);
		return ResponseEntity.ok(deviceService.list(searchDto));
	}

	@GetMapping("/staffList") // 직원 진단기 아이콘 목록 테이블 호출 및 검색 :list
	public ResponseEntity<ListDto<DeviceStaffViewDto>> staffList() {
		log.info(" --- staffList called --- {}");
		return ResponseEntity.ok(deviceService.staffList());
	}

	@PostMapping("/statusTestingChange/{locid}")
	public ResponseEntity<HttpStatus> statusTestingChange(@PathVariable @Valid Long locid) {
		log.info(" --- statusTestingChange called --- {}");
		deviceService.statusTestingChange(locid);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/historyCheck")
	public ResponseEntity<HttpStatus> historyCheck(HttpSession session) {
		deviceService.historyCheck(session);
		return ResponseEntity.ok().build();
	}
}
