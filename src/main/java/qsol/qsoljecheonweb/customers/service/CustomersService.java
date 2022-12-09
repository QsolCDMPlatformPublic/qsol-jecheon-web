package qsol.qsoljecheonweb.customers.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qsol.qsoljecheonweb.common.model.request.CarSearchDto;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.customers.model.response.CustomerCarViewDto;
import qsol.qsoljecheonweb.customers.model.response.CustomersViewDto;
import qsol.qsoljecheonweb.customers.repository.CustomerCarRepository;
import qsol.qsoljecheonweb.customers.repository.CustomersRepository;
import qsol.qsoljecheonweb.domain.customers.CustomerCar;
import qsol.qsoljecheonweb.domain.customers.Customers;
import qsol.qsoljecheonweb.domain.manager.Manager;
import qsol.qsoljecheonweb.error.customers.*;
import qsol.qsoljecheonweb.error.manager.*;
import qsol.qsoljecheonweb.manager.interfaceManager.ManagerInterface;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.request.ManagerLoginDto;
import qsol.qsoljecheonweb.manager.model.response.ManagerViewDto;
import qsol.qsoljecheonweb.util.QsolMessageDigest;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
public class CustomersService {

	@Autowired
	private CustomersRepository customersRepository;

	@Autowired
	private CustomerCarRepository customerCarRepository;
	
	public ListDto<CustomersViewDto> list(CustomerSearchDto customerSearchDto) { // 고객 목록 테이블 가져오기 및 검색 :list
		Pageable pageable = PageRequest.of(customerSearchDto.getPageNo()-1, customerSearchDto.getCountPerPage());
		List<CustomersInterface> customersList;
		Long count = 0L;
		// 모든 고객 정보 가져오기
		if(customerSearchDto.getSearchName().equals("") && customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")) {
			customersList = customersRepository.list(pageable);
			count = customersRepository.listCount(customerSearchDto); } // 모든 고객 count 수 가져오기 :paging
		// 이름 검색
		else if(!customerSearchDto.getSearchName().equals("") && customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			customersList = customersRepository.searchNameList("%"+customerSearchDto.getSearchName()+"%", pageable);
			count = customersRepository.searchNameListCount("%"+customerSearchDto.getSearchName()+"%"); } // 조건(이름)에 맞는 고객 count 수 가져오기 :paging
		// 이름 + 제조사 검색
		else if(!customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			customersList = customersRepository.searchNameMakerList("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), pageable);
			count = customersRepository.searchNameMakerListCount("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker()); }
		// 제조사 검색
		else if(customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			customersList = customersRepository.searchMakerList(customerSearchDto.getSearchMaker(), pageable);
			count = customersRepository.searchMakerListCount(customerSearchDto.getSearchMaker()); }
		// 제조사 + 모델 검색
		else if(customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && !customerSearchDto.getSearchModel().equals("ALL")){
			customersList = customersRepository.searchMakerModelList(customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel(), pageable);
			count = customersRepository.searchMakerModelListCount(customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel()); }
		// 이름 + 제조사 + 모델 검색
		else {
			customersList = customersRepository.searchAllList("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel(), pageable);
			count = customersRepository.searchAllListCount("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel());
		}
		List<CustomersViewDto> list = null;
		if(customersList != null) {
			list = QsolModelMapper.map(customersList, CustomersViewDto.class);
		}
		ListDto<CustomersViewDto> result = ListDto.<CustomersViewDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	public ListDto<CustomerCarViewDto> diagnosticCustomerInfo(CustomerSearchDto customerSearchDto) { // 진단이 가능한 고객의 (+차량) 정보 테이블 가져오기
		Pageable pageable = PageRequest.of(customerSearchDto.getPageNo()-1, customerSearchDto.getCountPerPage());
		List<CustomersInterface> customersList;
		Long count = 0L;
		// 모든 고객 정보 가져오기
		if(customerSearchDto.getSearchName().equals("")) {
			customersList = customersRepository.diagnosticCustomerInfo(pageable);
			count = customersRepository.diagnosticCustomerInfoCount(customerSearchDto);
		} else {
			customersList = customersRepository.diagnosticCustomerInfoSearchName("%"+customerSearchDto.getSearchName()+"%", pageable);
			count = customersRepository.diagnosticCustomerInfoCountSearchName("%"+customerSearchDto.getSearchName()+"%");
		}
		List<CustomerCarViewDto> list = null;
		if(customersList != null) {
			list = QsolModelMapper.map(customersList, CustomerCarViewDto.class);
		}
		ListDto<CustomerCarViewDto> result = ListDto.<CustomerCarViewDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	public ListDto<CustomerCarViewDto> vehicleList(CarSearchDto carSearchDto, Long cid) { // 고객 차량 목록 테이블 가져오기 및 검색 :vehicleList
		Long count = 0L;
		Pageable pageable = PageRequest.of(carSearchDto.getPageNo()-1, carSearchDto.getCountPerPage());
		List<CustomersInterface> vehicleList = customerCarRepository.findByCustomerId(cid, pageable);
		count = customerCarRepository.vehicleListCount(cid);
		List<CustomerCarViewDto> list = null;
		if(vehicleList != null) {
			list = QsolModelMapper.map(vehicleList, CustomerCarViewDto.class);
		}
		ListDto<CustomerCarViewDto> result = ListDto.<CustomerCarViewDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	@Transactional // 고객 신규 등록 :create
	public void create(CustomersViewDto customersViewDto) /*throws NoSuchAlgorithmException*/ {
		int duplicateCheck = customersRepository.customerDuplicateCheck(customersViewDto.getCustomersName(), customersViewDto.getCustomersPhone()); // 중복 확인을 위한
		if(customersViewDto.isLessCharacters(customersViewDto.getCustomersName()) || customersViewDto.isLessCharacters(customersViewDto.getCustomersPhone())) {
			// 고객 이름 & 휴대폰이 4글자 아래면 에러
			throw new LessCharactersCustomersException();
		} else if(duplicateCheck > 0) { // 고객명과 휴대폰 번호가 동시 중복이면 에러
			throw new CustomersDuplicateException();
		} else {
			Customers customers = QsolModelMapper.map(customersViewDto, Customers.class);
			customersRepository.save(customers); 
		}
	}

	@Transactional // 고객 차량 신규 등록 :carCreate
	public void carCreate(CustomerCarViewDto customerCarViewDto, Long cid) throws NoSuchAlgorithmException {
		int duplicateCheck = customersRepository.vehicleDuplicateCheck(customerCarViewDto.getCustomersCarNumber()); // 중복 확인을 위한
		if(customerCarViewDto.isLessCharacters(customerCarViewDto.getCustomersCarNumber()) || customerCarViewDto.isLessCharacters(customerCarViewDto.getCustomersCarYear())) {
			// 차량 번호 또는 차랑 연식이 4글자 아래면 에러
			throw new LessCharactersVehicleException();
		} else if(duplicateCheck > 0) { // 차량 번호가 중복이면 에러
			throw new VehicleDuplicateException();
		} else if(customerCarViewDto.getCustomersCarMaker().equals("") || customerCarViewDto.getCustomersCarName().equals("")) {
			throw new VehicleMakerAndNameException();
		} else {
			CustomerCar customerCar = QsolModelMapper.map(customerCarViewDto, CustomerCar.class);
			customerCar.setCustomerId(cid);
			customerCarRepository.save(customerCar);
		}
	}

	public List<CustomersInterface> view(Long cid) { // 고객 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
		List<CustomersInterface> customers = customersRepository.findByCid(cid);
		return customers;
	}

	public List<CustomersInterface> carView(Long carId) { // 차량 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
		List<CustomersInterface> vehicle = customerCarRepository.findByCustomersCarNumber(carId);
		return vehicle;
	}


	@Transactional // 고객 업데이트 :update
	public void update(CustomersViewDto customersViewDto) throws NoSuchAlgorithmException  {
		if(customersViewDto.isLessCharacters(customersViewDto.getCustomersName()) || customersViewDto.isLessCharacters(customersViewDto.getCustomersPhone())) {
			// 고객 이름 & 휴대폰이 4글자 아래면 에러
			throw new LessCharactersCustomersException();
		} else {
			Customers customers = customersRepository.updateForSelect(customersViewDto.getCid());
			if(customers != null) {
				CustomersViewDto update = new CustomerCarViewDto();
				update.setCid(customers.getId());
				update.setCustomersName(customersViewDto.getCustomersName());
				update.setCustomersPhone(customersViewDto.getCustomersPhone());
				update.setCustomersAddress1(customersViewDto.getCustomersAddress1());
				update.setCustomersAddress2(customersViewDto.getCustomersAddress2());
				update.setCustomersZipcode(customersViewDto.getCustomersZipcode());
				update.setCustomersCountry(customersViewDto.getCustomersCountry());
				QsolModelMapper.map(update, customers);
			}
		}
	}

	@Transactional // 차량 업데이트 :carUpdate
	public void carUpdate(CustomerCarViewDto customerCarViewDto) throws NoSuchAlgorithmException  {
		if(customerCarViewDto.isLessCharacters(customerCarViewDto.getCustomersCarNumber())) {
			// 차량 번호 4글자 아래면 에러
			throw new LessCharactersVehicleException();
		} else if(customerCarViewDto.getCustomersCarMaker().equals("") || customerCarViewDto.getCustomersCarName().equals("")) {
			throw new VehicleMakerAndNameException();
		} else {
			// 더티체킹을 위해 DB와 영속성 연결 시도. 차량 아이디를 기준으로 데이터를 갖고옴
			CustomerCar customerCar = customerCarRepository.carUpdateForSelect(customerCarViewDto.getCarId());
			if(customerCar != null) {
				CustomerCarViewDto update = new CustomerCarViewDto();
				update.setCustomerId(customerCar.getCustomerId());
				update.setCustomersCarNumber(customerCarViewDto.getCustomersCarNumber());
				update.setCustomersCarMaker(customerCarViewDto.getCustomersCarMaker());
				update.setCustomersCarName(customerCarViewDto.getCustomersCarName());
				update.setCustomersCarYear(customerCarViewDto.getCustomersCarYear());
				update.setCustomersCarMileage(customerCarViewDto.getCustomersCarMileage());
				QsolModelMapper.map(update, customerCar);
			}
		}
	}

	@Transactional
    public void customersDelete(Long cid) {
		customerCarRepository.deleteByCustomerId(cid); // 고객 삭제시 해당 고객의 차량을 삭제
		customersRepository.deleteById(cid); // 고객 삭제
    }

	@Transactional
	public void vehicleDelete(Long carId) {
		customerCarRepository.deleteById(carId);
	}
}
