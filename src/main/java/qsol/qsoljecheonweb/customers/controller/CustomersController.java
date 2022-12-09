package qsol.qsoljecheonweb.customers.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.common.model.request.CarSearchDto;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.customers.model.response.CustomerCarViewDto;
import qsol.qsoljecheonweb.customers.model.response.CustomersViewDto;
import qsol.qsoljecheonweb.customers.service.CustomersService;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.response.ManagerViewDto;
import qsol.qsoljecheonweb.manager.service.ManagerService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/system/customers")
public class CustomersController {

	@Autowired
	private CustomersService customersService;

	@GetMapping("/list") // 고객 목록 테이블 호출 및 검색 :list | :search
	public ResponseEntity<ListDto<CustomersViewDto>> list(CustomerSearchDto customerSearchDto) {
		log.info(" --- customers list called --- {}", customerSearchDto);
		customerSearchDto.setSearchName(customerSearchDto.getSearchName().trim()); // 공백제거
		return ResponseEntity.ok(customersService.list(customerSearchDto));
	}

	@GetMapping("/diagnosticCustomerInfo") // 진단이 가능한 고객의 (+차량) 정보 테이블 가져오기
	public ResponseEntity<ListDto<CustomerCarViewDto>> diagnosticCustomerInfo(CustomerSearchDto customerSearchDto) {
		log.info(" --- diagnosticCustomerInfo called --- {}", customerSearchDto);
		customerSearchDto.setSearchName(customerSearchDto.getSearchName().trim()); // 공백제거
		return ResponseEntity.ok(customersService.diagnosticCustomerInfo(customerSearchDto));
	}

	@GetMapping("/vehicleList/{cid}") // 고객 차량 목록 테이블 호출 및 검색 :vehicleList
	public ResponseEntity<ListDto<CustomerCarViewDto>> vehicleList(CarSearchDto carSearchDto, @PathVariable @Valid Long cid) {
		log.info(" --- vehicle list called --- {}", carSearchDto);
		return ResponseEntity.ok(customersService.vehicleList(carSearchDto, cid));
	}

	@PostMapping("/create") // 고객 신규 등록
	public ResponseEntity<HttpStatus> create(@RequestBody @Valid CustomersViewDto customersViewDto) throws NoSuchAlgorithmException {
		log.info(" --- customers create called --- {}", customersViewDto);
		customersViewDto.setCustomersName(customersViewDto.getCustomersName().trim()); // 공백제거
		customersViewDto.setCustomersPhone(customersViewDto.getCustomersPhone().trim());
		customersViewDto.setCustomersAddress1(customersViewDto.getCustomersAddress1().trim());
		customersViewDto.setCustomersAddress2(customersViewDto.getCustomersAddress2().trim());
		customersViewDto.setCustomersZipcode(customersViewDto.getCustomersZipcode().trim());
		customersViewDto.setCustomersCountry(customersViewDto.getCustomersCountry().trim());
		customersService.create(customersViewDto);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/carCreate/{cid}") // 고객 차량 신규 등록
	public ResponseEntity<HttpStatus> carCreate(@RequestBody @Valid CustomerCarViewDto customerCarViewDto, @PathVariable @Valid Long cid) throws NoSuchAlgorithmException {
		log.info(" --- vehicle create called --- {}", customerCarViewDto, cid);
		customerCarViewDto.setCustomersCarNumber(customerCarViewDto.getCustomersCarNumber().trim()); // 공백 제거
		customerCarViewDto.setCustomersCarYear(customerCarViewDto.getCustomersCarYear().trim());
		customersService.carCreate(customerCarViewDto, cid);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{cid}") // 고객 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
	public ResponseEntity<List<CustomersInterface>> view(@PathVariable @Valid Long cid) {
		log.info(" --- customers view called --- {}", cid);
		return ResponseEntity.ok(customersService.view(cid));
	}

	@PostMapping("/vehicle/{carId}") // 차량 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :carView
	public ResponseEntity<List<CustomersInterface>> carView(@PathVariable @Valid Long carId) {
		log.info(" --- vehicle view called --- {}", carId);
		return ResponseEntity.ok(customersService.carView(carId));
	}

	@PutMapping()
	public ResponseEntity<HttpStatus> update(@RequestBody @Valid CustomersViewDto customersViewDto) throws NoSuchAlgorithmException {
		log.info(" --- customers update called --- {}", customersViewDto);
		customersViewDto.setCustomersName(customersViewDto.getCustomersName().trim());
		customersViewDto.setCustomersPhone(customersViewDto.getCustomersPhone().trim());
		customersViewDto.setCustomersAddress1(customersViewDto.getCustomersAddress1().trim());
		customersViewDto.setCustomersAddress2(customersViewDto.getCustomersAddress2().trim());
		customersViewDto.setCustomersZipcode(customersViewDto.getCustomersZipcode().trim());
		customersViewDto.setCustomersCountry(customersViewDto.getCustomersCountry().trim());
		customersService.update(customersViewDto);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/vehicleUpdate")
	public ResponseEntity<HttpStatus> carUpdate(@RequestBody @Valid CustomerCarViewDto customerCarViewDto) throws NoSuchAlgorithmException {
		log.info(" --- vehicle update called --- {}", customerCarViewDto);
		customerCarViewDto.setCustomersCarNumber(customerCarViewDto.getCustomersCarNumber().trim()); // 공백 제거
		customersService.carUpdate(customerCarViewDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/customersDelete/{cid}")
	public ResponseEntity<HttpStatus> customersDelete(@PathVariable @Valid Long cid) {
		log.info(" --- customersDelete called --- {}", cid);
		customersService.customersDelete(cid);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/vehicleDelete/{carId}")
	public ResponseEntity<HttpStatus> vehicleDelete(@PathVariable @Valid Long carId) {
		log.info(" --- vehicleDelete called --- {}", carId);
		customersService.vehicleDelete(carId);
		return ResponseEntity.ok().build();
	}


}
