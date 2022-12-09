package qsol.qsoljecheonweb.result.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.diagnosis.repository.TestResultRepository;
import qsol.qsoljecheonweb.result.interfaceResult.ResultInterface;
import qsol.qsoljecheonweb.result.interfaceResult.ResultViewInterface;
import qsol.qsoljecheonweb.result.model.response.ResultListDto;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import java.util.List;

@Slf4j
@Service
public class ResultService {

	@Autowired
	private TestResultRepository testResultRepository;

	public ListDto<ResultListDto> list(CustomerSearchDto customerSearchDto) { // result 목록 테이블 가져오기 및 검색 :list
		Pageable pageable = PageRequest.of(customerSearchDto.getPageNo()-1, customerSearchDto.getCountPerPage());
		List<ResultInterface> resultList;
		Long count;
		// 모든 result 정보 가져오기
		if(customerSearchDto.getSearchName().equals("") && customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")) {
			resultList = testResultRepository.list(pageable);
			count = testResultRepository.listCount(customerSearchDto); } // 모든 result count 수 가져오기 :paging
		// 이름 검색
		else if(!customerSearchDto.getSearchName().equals("") && customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			resultList = testResultRepository.searchNameList("%"+customerSearchDto.getSearchName()+"%", pageable);
			count = testResultRepository.searchNameListCount("%"+customerSearchDto.getSearchName()+"%"); } // 조건(이름)에 맞는 result count 수 가져오기 :paging
		// 이름 + 제조사 검색
		else if(!customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			resultList = testResultRepository.searchNameMakerList("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), pageable);
			count = testResultRepository.searchNameMakerListCount("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker()); }
		// 제조사 검색
		else if(customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && customerSearchDto.getSearchModel().equals("ALL")){
			resultList = testResultRepository.searchMakerList(customerSearchDto.getSearchMaker(), pageable);
			count = testResultRepository.searchMakerListCount(customerSearchDto.getSearchMaker()); }
		// 제조사 + 모델 검색
		else if(customerSearchDto.getSearchName().equals("") && !customerSearchDto.getSearchMaker().equals("ALL") && !customerSearchDto.getSearchModel().equals("ALL")){
			resultList = testResultRepository.searchMakerModelList(customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel(), pageable);
			count = testResultRepository.searchMakerModelListCount(customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel()); }
		// 이름 + 제조사 + 모델 검색
		else {
			resultList = testResultRepository.searchAllList("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel(), pageable);
			count = testResultRepository.searchAllListCount("%"+customerSearchDto.getSearchName()+"%", customerSearchDto.getSearchMaker(), customerSearchDto.getSearchModel());
		}
		List<ResultListDto> list = null;
		if(resultList != null) {
			list = QsolModelMapper.map(resultList, ResultListDto.class);
		}
		ListDto<ResultListDto> result = ListDto.<ResultListDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	public ResultViewInterface result(int testid) {
		ResultViewInterface resultViewInterface = testResultRepository.result(testid);
		return resultViewInterface;
	}
}
