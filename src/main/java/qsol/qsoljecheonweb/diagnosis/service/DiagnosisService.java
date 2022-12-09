package qsol.qsoljecheonweb.diagnosis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qsol.qsoljecheonweb.code.repository.CodeInfoRepository;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.response.CustomerCarViewDto;
import qsol.qsoljecheonweb.customers.repository.CustomerCarRepository;
import qsol.qsoljecheonweb.customers.repository.CustomersRepository;
import qsol.qsoljecheonweb.device.interfaceDevice.DataSaveInfoInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.GuestDataSaveInfoInterface;
import qsol.qsoljecheonweb.device.repository.DeviceRepository;
import qsol.qsoljecheonweb.device.repository.DeviceStatusRepository;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.DiagnosisInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.RealTimeDataInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.ReportDataInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.TestMasterInterface;
import qsol.qsoljecheonweb.diagnosis.model.DataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.model.GuestDataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.model.response.DiagnosisViewDto;
import qsol.qsoljecheonweb.diagnosis.model.response.GuestDataDto;
import qsol.qsoljecheonweb.diagnosis.repository.TestMasterRepository;
import qsol.qsoljecheonweb.diagnosis.repository.TestResultRepository;
import qsol.qsoljecheonweb.error.diagnosis.AccessPermissionException;
import qsol.qsoljecheonweb.error.diagnosis.AllreadyToMeException;
import qsol.qsoljecheonweb.error.diagnosis.AnotherManagerConnectException;
import qsol.qsoljecheonweb.error.diagnosis.DisconeectionException;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class DiagnosisService {

    @Autowired
    private TestResultRepository testResultRepository;
    @Autowired
    private DeviceStatusRepository deviceStatusRepository;
    @Autowired
    private CustomerCarRepository customerCarRepository;
    @Autowired
    private CustomersRepository customersRepository;
    @Autowired
    private CodeInfoRepository codeInfoRepository;
    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private TestMasterRepository testMasterRepository;

    public ListDto<DiagnosisViewDto> diagnosisResultList(SearchDto searchDto, Long cid) { // customers list의 고객 진단 결과 팝업 테이블
        Pageable pageable = PageRequest.of(searchDto.getPageNo()-1, searchDto.getCountPerPage());
        Long count = 0L;
        List<DiagnosisInterface> diagnosisList = testResultRepository.diagnosisResultList(cid, pageable);
        count = testResultRepository.diagnosisResultListCount(cid);
        List<DiagnosisViewDto> list = null;
        if(diagnosisList != null) {
            list = QsolModelMapper.map(diagnosisList, DiagnosisViewDto.class);
        }
        ListDto<DiagnosisViewDto> result = ListDto.<DiagnosisViewDto>builder()
                .list(list)
                .count(count)
                .build();
        return result;
    }

    // 진단을 위한 진단기 선택 시 해당 진단기 상태 변경
    @Transactional
    public void statusChange(Long locid, String managerId, HttpSession session) {
        int statusCd = deviceStatusRepository.getStats(locid);
        String dBmanagerId = deviceStatusRepository.getManagerId(locid);
        if(dBmanagerId != null) {
            if(statusCd == 2) {
                if(managerId.equals(dBmanagerId)){
                    deviceStatusRepository.onlyConnecting(locid);
                } else {
                    throw new AccessPermissionException(dBmanagerId);
                }
            } else if(statusCd == 1) {
                if(managerId.equals(dBmanagerId)){
                    throw new AllreadyToMeException();
                } else {
                    throw new AnotherManagerConnectException();
                }
            } else {
                deviceStatusRepository.statusChange(locid, managerId);
            }
        }
        //String connectDevice = deviceRepository.findMacAddress(locid);
        //session.setAttribute("connectDevice", "tester-" + connectDevice + " Connected");
    }

    // 진단을 위한 고객 테이블 생성 실패 시 진단기 상태 리셋
    @Transactional
    public void statusReset(Long locid, String managerId, HttpSession session) {
        int statusCd = deviceStatusRepository.getStats(locid);
        String dBmanagerId = deviceStatusRepository.getManagerId(locid);
        if(dBmanagerId != null) {
            if(managerId.equals("stopDevice")) {
                deviceStatusRepository.statusReset(locid);
                //session.setAttribute("connectDevice", "");
            } else {
                if(statusCd == 2) {
                    if(!managerId.equals(dBmanagerId)) {
                        throw new DisconeectionException();
                    } else {
                        deviceStatusRepository.onlyConnectReset(locid);
                        //session.setAttribute("connectDevice", "");
                    }
                } else {
                    if(!managerId.equals(dBmanagerId)) {
                        throw new DisconeectionException();
                    } else {
                        deviceStatusRepository.statusReset(locid);
                        //session.setAttribute("connectDevice", "");
                    }
                }
            }
        }
    }

    // 진단을 위한 고객 차량 선택 시 해당 carId를 기준으로 해당 차량의 정보를 세션에 저장
    public List<CustomerCarViewDto> vehicleDataSaveForTestStart(Long carId, HttpSession session) {
        List<CustomersInterface> data = customerCarRepository.findAllForSessionSave(carId);
        if(data != null) {
            session.setAttribute("vehicleNo", data.get(0).getCustomersCarNumber());
            session.setAttribute("varcd", data.get(0).getCustomersCarMaker());
            session.setAttribute("vmlcd", data.get(0).getCustomersCarName());
            session.setAttribute("carYear", data.get(0).getCustomersCarYear());
            session.setAttribute("mileage", data.get(0).getCustomersCarMileage());
        }
        return null;
    }

    // 고객 아이디를 기준으로 이름을 찾고, id와 이름을 세션에 저장
    public void customerNameSessionSave(Long cid, HttpSession session) {
        String customerName = customersRepository.customerName(cid);
        session.setAttribute("cid", cid);
        if(customerName != null) {
            session.setAttribute("customerName", customerName);
        }
    }

    // code를 기준으로 codenm을 찾고 세션에 저장
    public void getCodeName(String varcd, String vmlcd, HttpSession session) {
        String varCodeNm = codeInfoRepository.getCodeNm(varcd);
        String vmlCodeNm = codeInfoRepository.getCodeNm(vmlcd);
        if(varCodeNm != null && vmlCodeNm != null) {
            session.setAttribute("varCodeNm", varCodeNm);
            session.setAttribute("vmlCodeNm", vmlCodeNm);
        }
    }

    // 고객 id와 차량 번호를 기준으로 차량 주행거리를 업데이트
    public void mileageChange(Long cid, Long mileage, String vehicleNo) {
        customerCarRepository.mileageChange(cid, mileage, vehicleNo);
    }

    // 테스트 시작 프로시저 호출
    public void testStart(HttpSession session) {
        Long locid = (Long) session.getAttribute("locid");
        Long customerId = (Long) session.getAttribute("cid");
        String vehicleNo = (String) session.getAttribute("vehicleNo");
        String varcd = (String) session.getAttribute("varcd");
        String vmlcd = (String) session.getAttribute("vmlcd");
        String carYear = (String) session.getAttribute("carYear");
        Long mileage = (Long) session.getAttribute("mileage");
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        String managerId = managerInfo.getManagerId();
        testResultRepository.testStart(locid, customerId, vehicleNo, varcd, vmlcd, carYear, mileage, managerId);
    }

    public void guestTestStart(GuestDataDto guestDataDto, HttpSession session) {
        //Long locid = (Long) session.getAttribute("locid");
        Long locid = guestDataDto.getLocid();
        Long customerId = 0L;
        String vehicleNo = guestDataDto.getVehicleNo();
        String varcd = guestDataDto.getCarMaker();
        String vmlcd = guestDataDto.getCarName();
        Long carYear = guestDataDto.getCarYear();
        String mileageTemp = guestDataDto.getCarMileage();
        Long mileage = Long.parseLong(mileageTemp);
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        String managerId = managerInfo.getManagerId();
        testResultRepository.guestTestStart(locid, customerId, vehicleNo, varcd, vmlcd, carYear, mileage, managerId);
    }


    public TestMasterInterface updConnectionForData(HttpSession session) {
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        TestMasterInterface testMasterInterface = testMasterRepository.updConnectionForData(managerInfo.getManagerId());
        return testMasterInterface;
    }

    // 테스트 시작시간과 현재 SOC, 진단 상태 정보 가져오기
    public List<RealTimeDataInterface> startDtAndSocAndStepCd(int testid) {
        List<RealTimeDataInterface> timeDataInterface = testMasterRepository.startDtAndSocAndStepCd(testid);
        return timeDataInterface;
    }

    public void testStop(Long locid) {
        deviceStatusRepository.testStop(locid);
    }

    public ReportDataInterface report(Long locid) {
        int testid = testMasterRepository.getTestid(locid);
        ReportDataInterface reportDataInterface = testResultRepository.report(testid);
        return reportDataInterface;
    }

    public String testStartPageConnectDevice(String managerId) {
        String result = deviceStatusRepository.testStartPageConnectDevice(managerId);
        return "충전기 "+result+" Connected";
    }

    public void resetConnection(Long locid) {
        deviceStatusRepository.resetConnection(locid);
    }

    public String nowConnectDevice(String managerId) {
        String connectDevice = deviceStatusRepository.nowConnectDevice(managerId);
        return "충전기 " + connectDevice + " Connected";
    }

    public DataSaveInfo recallData(Long locid) {
        DataSaveInfoInterface dataSaveInfoInterface = testMasterRepository.recallData(locid);
        DataSaveInfo result = null;
        if(dataSaveInfoInterface != null) {
            result = QsolModelMapper.map(dataSaveInfoInterface, DataSaveInfo.class);
        }
        return result;
    }

    public GuestDataSaveInfo guestRecallData(Long locid) {
        GuestDataSaveInfoInterface guestDataSaveInfoInterface = testMasterRepository.guestRecallData(locid);
        GuestDataSaveInfo result = null;
        if(guestDataSaveInfoInterface != null) {
            result = QsolModelMapper.map(guestDataSaveInfoInterface, GuestDataSaveInfo.class);
        }
        return result;
    }

    public String returnForCustomerName(Long locid) {
        String item = testMasterRepository.returnForCustomerName(locid);
        return item;
    }


    public void stopProcedureCall(Long locid, HttpSession session) {
        Long testid = testMasterRepository.stopProcedureCallForTestidFind(locid);
        if(testid != null) {
            testResultRepository.stopProcedureCall(testid, 0, "", "", 0, 0, "", 0, 0, 1, "서버와의 통신(연결) 오류");
        }
    }
}
