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

    public ListDto<DiagnosisViewDto> diagnosisResultList(SearchDto searchDto, Long cid) { // customers list??? ?????? ?????? ?????? ?????? ?????????
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

    // ????????? ?????? ????????? ?????? ??? ?????? ????????? ?????? ??????
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

    // ????????? ?????? ?????? ????????? ?????? ?????? ??? ????????? ?????? ??????
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

    // ????????? ?????? ?????? ?????? ?????? ??? ?????? carId??? ???????????? ?????? ????????? ????????? ????????? ??????
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

    // ?????? ???????????? ???????????? ????????? ??????, id??? ????????? ????????? ??????
    public void customerNameSessionSave(Long cid, HttpSession session) {
        String customerName = customersRepository.customerName(cid);
        session.setAttribute("cid", cid);
        if(customerName != null) {
            session.setAttribute("customerName", customerName);
        }
    }

    // code??? ???????????? codenm??? ?????? ????????? ??????
    public void getCodeName(String varcd, String vmlcd, HttpSession session) {
        String varCodeNm = codeInfoRepository.getCodeNm(varcd);
        String vmlCodeNm = codeInfoRepository.getCodeNm(vmlcd);
        if(varCodeNm != null && vmlCodeNm != null) {
            session.setAttribute("varCodeNm", varCodeNm);
            session.setAttribute("vmlCodeNm", vmlCodeNm);
        }
    }

    // ?????? id??? ?????? ????????? ???????????? ?????? ??????????????? ????????????
    public void mileageChange(Long cid, Long mileage, String vehicleNo) {
        customerCarRepository.mileageChange(cid, mileage, vehicleNo);
    }

    // ????????? ?????? ???????????? ??????
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

    // ????????? ??????????????? ?????? SOC, ?????? ?????? ?????? ????????????
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
        return "????????? "+result+" Connected";
    }

    public void resetConnection(Long locid) {
        deviceStatusRepository.resetConnection(locid);
    }

    public String nowConnectDevice(String managerId) {
        String connectDevice = deviceStatusRepository.nowConnectDevice(managerId);
        return "????????? " + connectDevice + " Connected";
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
            testResultRepository.stopProcedureCall(testid, 0, "", "", 0, 0, "", 0, 0, 1, "???????????? ??????(??????) ??????");
        }
    }
}
