package qsol.qsoljecheonweb.device.service;

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
import qsol.qsoljecheonweb.device.interfaceDevice.DataSaveInfoInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.DeviceInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.DeviceStaffInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.HistoryInterface;
import qsol.qsoljecheonweb.device.model.response.DeviceStaffViewDto;
import qsol.qsoljecheonweb.device.model.response.DeviceViewDto;
import qsol.qsoljecheonweb.device.repository.DeviceRepository;
import qsol.qsoljecheonweb.device.repository.DeviceStatusRepository;
import qsol.qsoljecheonweb.diagnosis.model.DataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.repository.TestMasterRepository;
import qsol.qsoljecheonweb.diagnosis.service.DiagnosisService;
import qsol.qsoljecheonweb.domain.customers.CustomerCar;
import qsol.qsoljecheonweb.domain.customers.Customers;
import qsol.qsoljecheonweb.error.customers.*;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static java.lang.Long.parseLong;

@Slf4j
@Service
public class DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;
	@Autowired
	private DeviceStatusRepository deviceStatusRepository;
	@Autowired
	private TestMasterRepository testMasterRepository;
	@Autowired
	private CustomersRepository customersRepository;
	@Autowired
	private DiagnosisService diagnosisService;

	public ListDto<DeviceViewDto> list(SearchDto searchDto) { // 진단기 목록 테이블 가져오기 :list
		Pageable pageable = PageRequest.of(searchDto.getPageNo() - 1, searchDto.getCountPerPage());
		Long count = 0L;
		List<DeviceInterface> deviceList = deviceRepository.list(pageable);
		count = deviceRepository.listCount(searchDto); // count 수 가져오기 :paging
		List<DeviceViewDto> list = null;
		if(deviceList != null) {
			list = QsolModelMapper.map(deviceList, DeviceViewDto.class);
		}
		ListDto<DeviceViewDto> result = ListDto.<DeviceViewDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	public ListDto<DeviceStaffViewDto> staffList() { // 진단기 목록 테이블 가져오기 :list
		List<DeviceStaffInterface> deviceList = deviceRepository.staffList();
		List<DeviceStaffViewDto> list = null;
		if(deviceList != null) {
			list = QsolModelMapper.map(deviceList, DeviceStaffViewDto.class);
		}
		ListDto<DeviceStaffViewDto> result = ListDto.<DeviceStaffViewDto>builder()
				.list(list)
				.build();
		return result;
	}

	public void statusTestingChange(Long locid) {
		deviceStatusRepository.statusTestingChange(locid);
	}

    public void historyCheck(HttpSession session) {
		String historyCheck = testMasterRepository.historyCheck();
		HistoryInterface historyInterface = testMasterRepository.historyDataGet();
		if(historyCheck != null && historyInterface != null) {
			session.setAttribute("locid", historyInterface.getLocid());
			if(historyCheck.equals("FALSE")) {
				session.setAttribute("testid", historyInterface.getTestid());
				Long cid = parseLong(historyInterface.getCid());

				DataSaveInfoInterface dataSaveInfoInterface = customersRepository.historyCheck(cid);
				if(dataSaveInfoInterface != null) {
					DataSaveInfo dataSaveInfo = new DataSaveInfo();
					dataSaveInfo.setLocid((Long) session.getAttribute("locid"));
					dataSaveInfo.setCid(dataSaveInfoInterface.getCid());
					dataSaveInfo.setCustomerName(dataSaveInfoInterface.getCustomerName());
					dataSaveInfo.setVehicleNo(dataSaveInfoInterface.getVehicleNo());
					dataSaveInfo.setVarcd(dataSaveInfoInterface.getVarcd());
					dataSaveInfo.setVmlcd(dataSaveInfoInterface.getVmlcd());
					diagnosisService.getCodeName(dataSaveInfoInterface.getVarcd(), dataSaveInfoInterface.getVmlcd(), session);
					dataSaveInfo.setVarCodeNm((String) session.getAttribute("varCodeNm"));
					dataSaveInfo.setVmlCodeNm((String) session.getAttribute("vmlCodeNm"));
					dataSaveInfo.setCarYear(dataSaveInfoInterface.getCarYear());
					dataSaveInfo.setMileage(dataSaveInfoInterface.getMileage());
					dataSaveInfo.setTestid(historyInterface.getTestid());

					String sessionName = dataSaveInfo.getLocid().toString();
					session.setAttribute(sessionName, dataSaveInfo);
					session.setAttribute("customerNameOfLocidToReturn"+dataSaveInfo.getLocid(), dataSaveInfo.getCustomerName());
				}
			} else {
				String connectManagerId = deviceStatusRepository.connectManagerId();
				if(connectManagerId != null) {
					if(connectManagerId.equals("root") || connectManagerId.equals("manager")) {
						diagnosisService.resetConnection(historyInterface.getLocid());
					}
				}
			}
		}
    }
}
