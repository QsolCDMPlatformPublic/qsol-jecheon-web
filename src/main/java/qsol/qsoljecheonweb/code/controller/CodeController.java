package qsol.qsoljecheonweb.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodeInterface;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodegpCallInterpage;
import qsol.qsoljecheonweb.code.model.response.CarCodeViewDto;
import qsol.qsoljecheonweb.code.service.CodeService;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@RequestMapping("/api/code")
@Controller
public class CodeController {

    @Autowired
    private CodeService codeService;

    @GetMapping("/codeVarAndVmlList") // 차량 제조사 & 모델 코드 리스트 불러오기 :list
    public ResponseEntity<ListDto<CarCodeViewDto>> codeVarAndVmlList(SearchDto searchDto) {
        log.info(" --- codeVarAndVmlList called --- {}", searchDto);
        return ResponseEntity.ok(codeService.codeVarAndVmlList(searchDto));
    }

    @GetMapping("/countryCodeList") // Country 코드 리스트 불러오기 :list
    public ResponseEntity<ListDto<CarCodeViewDto>> countryCodeList(SearchDto searchDto) {
        log.info(" --- countryCodeList called --- {}", searchDto);
        return ResponseEntity.ok(codeService.countryCodeList(searchDto));
    }

    @GetMapping("/carCodegpCall") // 홈화면 시작시 차량 제조사 관련 코드 그룹(셀렉박스) 불러오기 :carCodegpCall
    public ResponseEntity<List<CarCodegpCallInterpage>> carCodegpCall() {
        log.info(" --- carCodegpCall called --- {}");
        return ResponseEntity.ok(codeService.carCodegpCall());
    }

    @PostMapping("/carCodeCreate") // 차량 관련 코드 신규 등록 :create
    public ResponseEntity<HttpStatus> carCodeCreate(@RequestBody @Valid CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        log.info(" --- carCodeCreate called --- {}", carCodeViewDto);
        carCodeViewDto.setCodenm(carCodeViewDto.getCodenm().toUpperCase().trim()); //코드그룹명 전부 대문자로 치환하여 저장하기
        codeService.carCodeCreate(carCodeViewDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/countryCreate") // country 코드 신규 등록 :create
    public ResponseEntity<HttpStatus> countryCreate(@RequestBody @Valid CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        log.info(" --- countryCreate called --- {}", carCodeViewDto);
        carCodeViewDto.setCodenm(carCodeViewDto.getCodenm().toUpperCase().trim()); //코드그룹명 전부 대문자로 치환하여 저장하기
        codeService.countryCreate(carCodeViewDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{code}") // 차량 관련 코드의 상세정보 팝업 노출을 위해 해당 코드의 데이터를 불러오기 :view
    public ResponseEntity<CarCodeViewDto> codeDataGet(@PathVariable @Valid String code) {
        log.info(" --- codeDataGet called --- {}", code);
        return ResponseEntity.ok(codeService.codeDataGet(code));
    }

    @PutMapping("/carCodeUpdate") // 차량 관련 코드 업데이트 :update
    public ResponseEntity<HttpStatus> carCodeUpdate(@RequestBody @Valid CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        log.info(" --- carCodeUpdate called --- {}", carCodeViewDto);
        carCodeViewDto.setCodenm(carCodeViewDto.getCodenm().toUpperCase().trim()); //코드그룹명 전부 대문자로 치환하여 저장하기
        if (carCodeViewDto.getCodegp().equals("vehicleMaker")) {
            carCodeViewDto.setCodegp("VAR");
        } else if (carCodeViewDto.getCodegp().equals("vehicleName")) {
            carCodeViewDto.setCodegp("VML");
        }
        codeService.carCodeUpdate(carCodeViewDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/countryUpdate") // country 코드 업데이트 :update
    public ResponseEntity<HttpStatus> countryUpdate(@RequestBody @Valid CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        log.info(" --- countryUpdate called --- {}", carCodeViewDto);
        carCodeViewDto.setCodenm(carCodeViewDto.getCodenm().toUpperCase().trim()); //코드그룹명 전부 대문자로 치환하여 저장하기
        carCodeViewDto.setCodegp("CTR");
        codeService.countryUpdate(carCodeViewDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/carCodeDelete/{code}") // 차량 관련 코드 삭제 :delete
    public ResponseEntity<HttpStatus> carCodeDelete(@PathVariable @Valid String code) {
        log.info(" --- carCodeDelete called --- {}", code);
        codeService.carCodeDelete(code);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/countryDelete/{code}") // country 코드 삭제 :delete
    public ResponseEntity<HttpStatus> countryDelete(@PathVariable @Valid String code) {
        log.info(" --- countryDelete called --- {}", code);
        return ResponseEntity.ok().build();
    }

    // 홈화면 시작시 차량 제조사 & 모델 코드 리스트(셀렉박스) 불러오기 :vehicleCodeList :getCodeSelectBox
    // + country 코드 셀렉 박스
    @GetMapping("/getCodeSelectBox/{item}")
    public ResponseEntity<List<CarCodegpCallInterpage>> getCodeSelectBox(@PathVariable @Valid String item) {
        log.info(" --- getCodeSelectBox called --- {}");
        return ResponseEntity.ok(codeService.getCodeSelectBox(item));
    }

    @GetMapping("/vehicleMaker")// 차량 모델 생성시 차량 제조사 이름 가져와서 셀렉박스 생성
    public ResponseEntity<List<CarCodeInterface>> vehicleMakerList() {
        log.info(" --- vehicleMakerList called --- {}");
        return ResponseEntity.ok(codeService.vehicleMakerList());
    }

    @GetMapping("/customerCarNameSelectBox/{customersCarMaker}") // 선택한 차량 제조사에 해당하는 차량 모델들을 가져옴
    public ResponseEntity<List<CarCodegpCallInterpage>> customerCarNameSelectBox(@PathVariable @Valid String customersCarMaker) {
        log.info(" --- customerCarNameSelectBox called --- {}");
        return ResponseEntity.ok(codeService.customerCarNameSelectBox(customersCarMaker));
    }

}
