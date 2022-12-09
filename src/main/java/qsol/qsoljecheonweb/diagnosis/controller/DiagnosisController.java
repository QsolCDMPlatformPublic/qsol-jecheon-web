package qsol.qsoljecheonweb.diagnosis.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.model.response.CustomerCarViewDto;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.RealTimeDataInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.ReportDataInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.TestMasterInterface;
import qsol.qsoljecheonweb.diagnosis.model.DataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.model.GuestDataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.model.response.DiagnosisViewDto;
import qsol.qsoljecheonweb.diagnosis.model.response.GuestDataDto;
import qsol.qsoljecheonweb.diagnosis.model.response.SendDataViewDto;
import qsol.qsoljecheonweb.diagnosis.service.DiagnosisService;
import qsol.qsoljecheonweb.diagnosis.udp.UDPEchoClient;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.SocketException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/diagnosis")
public class DiagnosisController {

    @Autowired
    private DiagnosisService diagnosisService;

    // customers list의 고객 진단 결과 팝업 테이블
    @GetMapping("/{cid}")
    public ResponseEntity<ListDto<DiagnosisViewDto>> diagnosisResultList(SearchDto searchDto, @PathVariable @Valid Long cid) {
        log.info(" --- diagnosis list called --- {}", searchDto, cid);
        return ResponseEntity.ok(diagnosisService.diagnosisResultList(searchDto, cid));
    }

    // 진단을 위한 진단기 선택 시 해당 진단기 locid를 세션에 저장 후 진단기 상태를 진행중으로 변경
    @PostMapping("/locidSaveForTestStart/{locid}/{managerId}")
    public ResponseEntity<HttpStatus> locidSaveForTestStart(@PathVariable @Valid Long locid, @PathVariable @Valid String managerId, HttpSession session) {
        log.info(" --- locidSaveForTestStart called ---{}", locid, managerId);
        session.setAttribute("locid", locid);
        diagnosisService.statusChange(locid, managerId, session);
        return null;
    }

    @PostMapping("/autoAction/{locid}")
    public ResponseEntity<HttpStatus> autoAction(@PathVariable @Valid Long locid, HttpSession session) {
        log.info(" --- autoAction called ---{}", locid);
        session.setAttribute("locid", locid);
        return null;
    }

    @PostMapping("/actionChange/{locid}/{managerId}/{oldLocid}")
    public ResponseEntity<HttpStatus> actionChange(@PathVariable @Valid Long locid, @PathVariable @Valid String managerId, @PathVariable @Valid Long oldLocid, HttpSession session) {
        log.info(" --- actionChange called ---{}", locid, managerId, oldLocid);
        diagnosisService.statusReset(oldLocid, managerId, session);
        session.setAttribute("locid", locid);
        diagnosisService.statusChange(locid, managerId, session);
        return null;
    }

    @GetMapping("/nowConnectDevice/{managerId}")
    public String nowConnectDevice(@PathVariable @Valid String managerId, HttpSession session) {
        log.info(" --- nowConnectDevice called ---{}");
        //String connectDevice = (String) session.getAttribute("connectDevice");
        String connectDevice = diagnosisService.nowConnectDevice(managerId);
        return connectDevice;
    }


    // 진단기의 상태를 대기로 리셋
    @PutMapping("/statusReset/{locid}/{managerId}")
    public ResponseEntity<HttpStatus> statusReset(@PathVariable @Valid Long locid, @PathVariable @Valid String managerId, HttpSession session) {
        log.info(" --- statusReset called ---{}", locid);
        diagnosisService.statusReset(locid, managerId, session);
        //diagnosisService.statusReset(locid);
        //session.setAttribute("connectDevice", "");
        return null;
    }

    // 진단기 강제 중지 및 초기화
    @PutMapping("/stopDevice/{locid}")
    public ResponseEntity<HttpStatus> stopDevice(@PathVariable @Valid Long locid, HttpSession session) {
        log.info(" --- stopDevice called ---{}", locid);
        String managerId = "stopDevice";
        diagnosisService.statusReset(locid, managerId, session);
        //session.setAttribute("connectDevice", "");
        return null;
    }

    // 진단을 위한 고객 선택 시 해당 고객 cid와 고객 이름을 세션에 저장
    @PostMapping("/cidSaveForTestStart/{cid}")
    public ResponseEntity<HttpStatus> cidSaveForTestStart(@PathVariable @Valid Long cid, HttpSession session) {
        log.info(" --- cidSaveForTestStart called ---{}", cid);
        diagnosisService.customerNameSessionSave(cid, session);
        return null;
    }

    // 진단을 위한 고객 차량 선택 시 해당 carId를 기준으로 해당 차량의 정보를 세션에 저장
    @PostMapping("/vehicleDataSaveForTestStart/{carId}")
    public ResponseEntity<List<CustomerCarViewDto>> vehicleDataSaveForTestStart(@PathVariable @Valid Long carId, HttpSession session) {
        log.info(" --- vehicleDataSaveForTestStart called ---{}", carId);
        return ResponseEntity.ok(diagnosisService.vehicleDataSaveForTestStart(carId, session));
    }

    // 세션에 저장된 진단에 필요한 데이터들을 가져옴
    @GetMapping("/customerDiagnosisData")
    public SendDataViewDto totalDataGet(HttpSession session) {
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        SendDataViewDto sendDataViewDto = new SendDataViewDto();
        sendDataViewDto.setLocid((Long) session.getAttribute("locid"));
        sendDataViewDto.setCustomerId((Long) session.getAttribute("cid"));
        sendDataViewDto.setCustomerName((String) session.getAttribute("customerName"));
        sendDataViewDto.setVehicleNo((String) session.getAttribute("vehicleNo"));
        sendDataViewDto.setVarcd((String) session.getAttribute("varcd"));
        sendDataViewDto.setVmlcd((String) session.getAttribute("vmlcd"));
        diagnosisService.getCodeName(sendDataViewDto.getVarcd(), sendDataViewDto.getVmlcd(), session);
        sendDataViewDto.setVarCodeNm((String) session.getAttribute("varCodeNm"));
        sendDataViewDto.setVmlCodeNm((String) session.getAttribute("vmlCodeNm"));
        sendDataViewDto.setCarYear((String) session.getAttribute("carYear"));
        sendDataViewDto.setMileage((Long) session.getAttribute("mileage"));
        sendDataViewDto.setManagerId(managerInfo.getManagerId());
        return sendDataViewDto;
    }

    // 고객 id와 차량 번호를 기준으로 차량 주행거리를 업데이트
    @PutMapping("/mileageChange/{cid}/{mileage}/{vehicleNo}")
    public void mileageChange(@PathVariable @Valid Long cid, @PathVariable @Valid Long mileage, @PathVariable @Valid String vehicleNo, HttpSession session) {
        diagnosisService.mileageChange(cid, mileage, vehicleNo);
        session.setAttribute("mileage", mileage);
    }

    @PostMapping("/testStart")
    public ResponseEntity<HttpStatus> testStart(HttpSession session) throws SocketException {
        log.info(" --- testStart called ---{}");
        log.info(" --- sp_test_start 프로시저 호출 시도");
        diagnosisService.testStart(session); // 테스트 시작 프로시저 호출
        log.info(" --- sp_test_start 프로시저 작업 완료, --- UDP 통신 시도 --- ");
        TestMasterInterface testMasterInterface = diagnosisService.updConnectionForData(session); // tb_test_master에 저장된 데이터 가져오기
        session.setAttribute("testid", testMasterInterface.getTestid());
        String stepCheck = "start";
        new UDPEchoClient("192.168.0.80", 20048, testMasterInterface.getLocid(), testMasterInterface.getTestid(), stepCheck);
        log.info(" --- UDP 통신 시도 완료 ---");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/guestTestStart")
    public ResponseEntity<HttpStatus> guestTestStart(@RequestBody @Valid GuestDataDto guestDataDto, HttpSession session) throws SocketException {
        log.info(" --- guestTestStart called ---{}");
        log.info(" --- sp_test_start 프로시저 호출 시도");
        diagnosisService.guestTestStart(guestDataDto, session); // 테스트 시작 프로시저 호출
        log.info(" --- sp_test_start 프로시저 작업 완료, --- UDP 통신 시도 --- ");
        TestMasterInterface testMasterInterface = diagnosisService.updConnectionForData(session); // tb_test_master에 저장된 데이터 가져오기
        session.setAttribute("testid", testMasterInterface.getTestid());
        String stepCheck = "start";
        new UDPEchoClient("192.168.0.80", 20048, testMasterInterface.getLocid(), testMasterInterface.getTestid(), stepCheck);
        log.info(" --- UDP 통신 시도 완료 ---");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stopProcedureCall/{locid}")
    public ResponseEntity<HttpStatus> stopProcedureCall(@PathVariable @Valid Long locid, HttpSession session) throws SocketException {
        log.info(" --- sp_test_end 프로시저 호출 시도");
        diagnosisService.stopProcedureCall(locid, session);
        log.info(" --- sp_test_end 프로시저 작업 완료, --- UDP 통신 시도 --- ");
        TestMasterInterface testMasterInterface = diagnosisService.updConnectionForData(session); // tb_test_master에 저장된 데이터 가져오기
        String stepCheck = "stop";
        new UDPEchoClient("192.168.0.80", 20048, testMasterInterface.getLocid(), testMasterInterface.getTestid(), stepCheck);
        log.info(" --- UDP 통신 시도 완료 ---");
        return ResponseEntity.ok().build();
    }

    // 테스트 시작시간과 현재 SOC, 진단 상태 정보 가져오기
    @GetMapping("/startDtAndSocAndStepCd")
    public ResponseEntity<List<RealTimeDataInterface>> startDtAndSocAndStepCd(HttpSession session) {
        int testid = (int) session.getAttribute("testid");
        return ResponseEntity.ok(diagnosisService.startDtAndSocAndStepCd(testid));
    }

    @PostMapping("/testStop/{locid}")
    public ResponseEntity<HttpStatus> testStop(@PathVariable @Valid Long locid, HttpSession session) throws SocketException {
        TestMasterInterface testMasterInterface = diagnosisService.updConnectionForData(session); // tb_test_master에 저장된 데이터 가져오기
        String stepCheck = "stop";
        new UDPEchoClient("192.168.0.80", 20048, testMasterInterface.getLocid(), testMasterInterface.getTestid(), stepCheck);
        diagnosisService.testStop(locid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getLocid") // 게스트 진행시 locid가져오기
    public Long getLocid(HttpSession session) {
        Long locid = (Long) session.getAttribute("locid");
        return locid;
    }

    /*@GetMapping("/dataSaveBeforeMoving")
    public ResponseEntity<HttpStatus> dataSaveBeforeMoving(HttpSession session) {
        DataSaveInfo dataSaveInfo = new DataSaveInfo();
        dataSaveInfo.setLocid((Long) session.getAttribute("locid"));
        dataSaveInfo.setCid((Long) session.getAttribute("cid"));
        dataSaveInfo.setCustomerName((String) session.getAttribute("customerName"));
        dataSaveInfo.setVehicleNo((String) session.getAttribute("vehicleNo"));
        dataSaveInfo.setVarcd((String) session.getAttribute("varcd"));
        dataSaveInfo.setVmlcd((String) session.getAttribute("vmlcd"));
        dataSaveInfo.setVarCodeNm((String) session.getAttribute("varCodeNm"));
        dataSaveInfo.setVmlCodeNm((String) session.getAttribute("vmlCodeNm"));
        dataSaveInfo.setCarYear((String) session.getAttribute("carYear"));
        dataSaveInfo.setMileage((Long) session.getAttribute("mileage"));
        dataSaveInfo.setTestid((int) session.getAttribute("testid"));

        String sessionName = dataSaveInfo.getLocid().toString();
        session.setAttribute(sessionName, dataSaveInfo);

        session.setAttribute("customerNameOfLocidToReturn"+dataSaveInfo.getLocid(), dataSaveInfo.getCustomerName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/guestDataSaveBeforeMoving")
    public ResponseEntity<HttpStatus> guestDataSaveBeforeMoving(@RequestBody @Valid GuestDataDto guestDataDto, HttpSession session) {
        GuestDataSaveInfo guestDataSaveInfo = new GuestDataSaveInfo();
        guestDataSaveInfo.setLocid(guestDataDto.getLocid());
        guestDataSaveInfo.setCid(0L);
        guestDataSaveInfo.setCustomerName("guest");
        guestDataSaveInfo.setVehicleNo(guestDataDto.getVehicleNo());
        guestDataSaveInfo.setCarMaker(guestDataDto.getCarMaker());
        guestDataSaveInfo.setCarName(guestDataDto.getCarName());
        *//*guestDataSaveInfo.setVarCodeNm();
        guestDataSaveInfo.setVmlCodeNm();*//*
        guestDataSaveInfo.setCarYear(guestDataDto.getCarYear());
        guestDataSaveInfo.setCarMileage(guestDataDto.getCarMileage());
        guestDataSaveInfo.setTestid((int) session.getAttribute("testid"));

        String sessionName = guestDataSaveInfo.getLocid().toString();
        session.setAttribute(sessionName, guestDataSaveInfo);

        session.setAttribute("customerNameOfLocidToReturn"+guestDataSaveInfo.getLocid(), guestDataSaveInfo.getCustomerName());
        return ResponseEntity.ok().build();
    }*/

    @GetMapping("/returnForCustomerName/{locid}")
    public Boolean returnForCustomerName(@PathVariable @Valid Long locid, HttpSession session) {
        //String customerName = (String) session.getAttribute("customerNameOfLocidToReturn"+locid);
        String item = diagnosisService.returnForCustomerName(locid);
        Boolean result;
        if(item.equals("guest")) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    /*@GetMapping("/returnToTest/{locid}")
    public Boolean returnToTest(@PathVariable @Valid Long locid, HttpSession session) {
        String sessionName = locid.toString();
        DataSaveInfo dataSaveInfo = (DataSaveInfo) session.getAttribute(sessionName);
        Boolean result;
        if(!sessionName.equals(dataSaveInfo.getLocid().toString())) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }

    @GetMapping("/guestReturnToTest/{locid}")
    public Boolean guestReturnToTest(@PathVariable @Valid Long locid, HttpSession session) {
        String sessionName = locid.toString();
        GuestDataSaveInfo guestDataSaveInfo = (GuestDataSaveInfo) session.getAttribute(sessionName);
        Boolean result;
        if(!sessionName.equals(guestDataSaveInfo.getLocid().toString())) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }*/

    @GetMapping("/recallData/{locid}")
    public ResponseEntity<DataSaveInfo> recallData(@PathVariable @Valid Long locid) {
        return ResponseEntity.ok(diagnosisService.recallData(locid));
    }

    @GetMapping("/guestRecallData/{locid}")
    public ResponseEntity<GuestDataSaveInfo> guestRecallData(@PathVariable @Valid Long locid) {
        return ResponseEntity.ok(diagnosisService.guestRecallData(locid));
    }

    @GetMapping("/report/{locid}")
    public ResponseEntity<ReportDataInterface> report(@PathVariable @Valid Long locid) {
        return ResponseEntity.ok(diagnosisService.report(locid));
    }

    @GetMapping("/guestGetLocid")
    public Long guestGetLocid(HttpSession session) {
        Long locid = (Long) session.getAttribute("locid");
        return locid;
    }

    @GetMapping("/testStartPageConnectDevice")
    public String testStartPageConnectDevice(HttpSession session) {
        ManagerInfo managerInfo = (ManagerInfo) session.getAttribute("manager-info");
        String result = diagnosisService.testStartPageConnectDevice(managerInfo.getManagerId());
        return result;
    }
}

