package qsol.qsoljecheonweb.code.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodeInterface;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodegpCallInterpage;
import qsol.qsoljecheonweb.code.model.response.CarCodeViewDto;
import qsol.qsoljecheonweb.code.repository.CodeGroupRepository;
import qsol.qsoljecheonweb.code.repository.CodeInfoRepository;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.domain.code.CodeInfo;
import qsol.qsoljecheonweb.error.code.*;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class CodeService {

    @Autowired
    private CodeInfoRepository codeInfoRepository; // tb_codeinfo

    @Autowired
    private CodeGroupRepository codeGroupRepository; // tb_codegroup

    public ListDto<CarCodeViewDto> codeVarAndVmlList(SearchDto searchDto) { // 차량 제조사+모델 코드 리스트 불러오기 :list
        Pageable pageable = PageRequest.of(searchDto.getPageNo()-1, searchDto.getCountPerPage());
        List<CarCodeInterface> carCodeList = codeInfoRepository.codeVarAndVmlList(pageable);
        List<CarCodeViewDto> carCodeListDtoList = null;
        Long count = 0L;
        if(carCodeList != null) {
            carCodeListDtoList = QsolModelMapper.map(carCodeList, CarCodeViewDto.class);
            count = codeInfoRepository.codeVarAndVmlCount(searchDto); // 차량 제조사+모델 코드 개수 불러오기 :paging
        }
        ListDto<CarCodeViewDto> result = ListDto.<CarCodeViewDto>builder()
                .list(carCodeListDtoList)
                .count(count)
                .build();
        return result;
    }

    public ListDto<CarCodeViewDto> countryCodeList(SearchDto searchDto) { // country 코드 리스트 불러오기 :list
        Pageable pageable = PageRequest.of(searchDto.getPageNo()-1, searchDto.getCountPerPage());
        List<CarCodeInterface> countryCodeList = codeInfoRepository.countryCodeList(pageable);
        List<CarCodeViewDto> countryCodeListDtoList = null;
        Long count = 0L;
        if(countryCodeList != null) {
            countryCodeListDtoList = QsolModelMapper.map(countryCodeList, CarCodeViewDto.class);
            count = codeInfoRepository.countryCodeListCount(searchDto); // country 코드 개수 불러오기 :paging
        }
        ListDto<CarCodeViewDto> result = ListDto.<CarCodeViewDto>builder()
                .list(countryCodeListDtoList)
                .count(count)
                .build();
        return result;
    }

    public List<CarCodegpCallInterpage> carCodegpCall() { // 홈화면 시작시 차량 제조사 관련 코드 그룹(셀렉박스) 불러오기 :carCodegpCall
        List<CarCodegpCallInterpage> carCodegpList = codeGroupRepository.carCodegpCall();
        return carCodegpList;
    }

    @Transactional // 차량 관련 코드 신규 등록 :create
    public void carCodeCreate(CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        if(carCodeViewDto.getCodegp().equals("")) { // 코드 그룹 셀렉박스 미선택 에러
            throw new CodegpSelectBoxNoChoiceException();
        } else if (carCodeViewDto.isLessCharactersCodenm(carCodeViewDto.getCodenm())) { // 코드 이름이 두글자 아래면 에러
            throw new LessCharactersCodenmException();
        } else if (carCodeViewDto.getReference() == null) {
            throw new ReferenceNullException();
        } else {
            String preCode = carCodeViewDto.getCodegp(); // ────────────────────────────────────────┐
            String postCode = codeInfoRepository.codeNumberForCreate(carCodeViewDto.getCodegp());// │
            if(postCode != null) {
                String code = carCodeViewDto.postCodeTypeChange(preCode, postCode);              // │
                // ↑ 코드 값 자동 생성을 위한 작업 ──────────────────────────────────────────────────────┘
                CodeInfo codeInfo = QsolModelMapper.map(carCodeViewDto, CodeInfo.class);
                codeInfo.setCode(code);
                codeInfo.setReference(carCodeViewDto.getReference());
                codeInfoRepository.save(codeInfo); // JPA Create
            }
        }
    }

    @Transactional // country 코드 신규 등록 :create
    public void countryCreate(CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        if (carCodeViewDto.isLessCharactersCodenm(carCodeViewDto.getCodenm())) { // 코드 이름이 두글자 아래면 에러
            throw new LessCharactersCodenmException();
        } else {
            String preCode = carCodeViewDto.getCodegp(); // ────────────────────────────────────────┐
            String postCode = codeInfoRepository.codeNumberForCreate(carCodeViewDto.getCodegp());// │
            if(postCode != null) {
                String code = carCodeViewDto.postCodeTypeChange(preCode, postCode);              // │
                // ↑ 코드 값 자동 생성을 위한 작업 ──────────────────────────────────────────────────────┘
                CodeInfo codeInfo = QsolModelMapper.map(carCodeViewDto, CodeInfo.class);
                codeInfo.setCode(code);
                codeInfoRepository.save(codeInfo); // JPA Create
            }
        }
    }

    public CarCodeViewDto codeDataGet(String code) { // 차량 관련 코드 상세정보 팝업 노출을 위해 해당 코드의 데이터를 불러오기 :view
        CodeInfo codeinfo = codeInfoRepository.findByCode(code);
        CarCodeViewDto result = null;
        if(codeinfo != null) {
            result = QsolModelMapper.map(codeinfo, CarCodeViewDto.class);
        }
        return result;
    }

    @Transactional // 차량 관련 코드 업데이트 :update
    public void carCodeUpdate(CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        if(carCodeViewDto.getCodegp().equals("")) { // 코드 그룹 셀렉박스 미선택 에러
            throw new CodegpSelectBoxNoChoiceException();
        } else if (carCodeViewDto.isLessCharactersCodenm(carCodeViewDto.getCodenm())) { // 코드 이름이 두글자 아래면 에러
            throw new LessCharactersCodenmException();
        } else {
            CodeInfo codeinfo = QsolModelMapper.map(carCodeViewDto, CodeInfo.class);
            codeInfoRepository.save(codeinfo); // JPA Update
        }
    }

    @Transactional // country 코드 업데이트 :update
    public void countryUpdate(CarCodeViewDto carCodeViewDto) throws NoSuchAlgorithmException {
        if (carCodeViewDto.isLessCharactersCodenm(carCodeViewDto.getCodenm())) { // 코드 이름이 두글자 아래면 에러
            throw new LessCharactersCodenmException();
        } else {
            CodeInfo codeinfo = QsolModelMapper.map(carCodeViewDto, CodeInfo.class);
            codeInfoRepository.save(codeinfo); // JPA Update
        }
    }

    @Transactional // 차량 관련 코드 삭제 :delete
    public void carCodeDelete(String code) {
        codeInfoRepository.deleteByCode(code); // JPA Delete
    }

    @Transactional // country 코드 삭제 :delete
    public void countryDelete(String code) {
        codeInfoRepository.deleteByCode(code); // JPA Delete
    }

    // 홈화면 시작시 차량 제조사 & 모델 코드 리스트(셀렉박스) 불러오기 :vehicleCodeList :getCodeSelectBox
    // + country 코드 셀렉 박스
    public List<CarCodegpCallInterpage> getCodeSelectBox(String item) {
        List<CarCodegpCallInterpage> getCodeSelectBox = null;
        if(item.equals("var")) {
            getCodeSelectBox = codeInfoRepository.varCodeListCall();
        } else {
            getCodeSelectBox = codeInfoRepository.ctrCodeListCall();
        }
        return getCodeSelectBox;
    }

    public List<CarCodeInterface> vehicleMakerList() {// 차량 모델 생성시 차량 제조사 이름 가져와서 셀렉박스 생성
        List<CarCodeInterface> vehicleMakerList = codeGroupRepository.vehicleMakerList();
        return vehicleMakerList;
    }

    public List<CarCodegpCallInterpage> customerCarNameSelectBox(String customersCarMaker) { // 선택한 차량 제조사에 해당하는 차량 모델들을 가져옴
        String reference = codeGroupRepository.referenceGet(customersCarMaker);
        List<CarCodegpCallInterpage> customerCarNameSelectBox = null;
        if(reference != null) {
            customerCarNameSelectBox = codeInfoRepository.customerCarNameSelectBox(reference);
        }
        return customerCarNameSelectBox;
    }
}
