package qsol.qsoljecheonweb.result.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.result.interfaceResult.ResultViewInterface;
import qsol.qsoljecheonweb.result.model.response.ResultListDto;
import qsol.qsoljecheonweb.result.service.ResultService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/system/result")
public class ResultController {

	@Autowired
	private ResultService resultService;

	@GetMapping("/list") // :list | :search
	public ResponseEntity<ListDto<ResultListDto>> list(CustomerSearchDto customerSearchDto) {
		log.info(" --- result list called --- {}", customerSearchDto);
		customerSearchDto.setSearchName(customerSearchDto.getSearchName().trim()); // 공백제거
		return ResponseEntity.ok(resultService.list(customerSearchDto));
	}

	@PostMapping("/{testid}")
	public ResponseEntity<ResultViewInterface> result(@PathVariable @Valid int testid) {
		return ResponseEntity.ok(resultService.result(testid));
	}


}
