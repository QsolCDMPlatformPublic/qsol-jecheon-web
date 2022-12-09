package qsol.qsoljecheonweb.manager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.domain.manager.Manager;
import qsol.qsoljecheonweb.error.manager.*;
import qsol.qsoljecheonweb.manager.interfaceManager.ManagerInterface;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.request.ManagerLoginDto;
import qsol.qsoljecheonweb.manager.model.response.ManagerViewDto;
import qsol.qsoljecheonweb.manager.repository.ManagerRepository;
import qsol.qsoljecheonweb.util.QsolMessageDigest;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
public class ManagerService {

	@Autowired
	private ManagerRepository managerRepository;

	public ManagerInfo login(ManagerLoginDto managerLoginDto) throws NoSuchAlgorithmException { // 로그인
		String managerIdCheck = managerRepository.managerIdCheck(managerLoginDto.getManagerId()); // 유저 아이디가 DB에 존재하는지 체크
		Boolean using = managerRepository.managerUsingCheck(managerLoginDto.getManagerId()); // 다른 기기에서 이미 로그인 했는지 체크
		if(managerIdCheck != null && using != null) {
			if(managerIdCheck.equals("NonExistentManagerId")) { // 유저 아이디가 DB에 존재하는지 체크
				throw new NonExistentManagerIdException();
			} else if(using != false) { // 다른 기기에서 이미 로그인 했는지 체크
				throw new DuplicateLoginException();
			}
		}
		return managerRepository.findById(managerLoginDto.getManagerId())
				.filter(u-> QsolMessageDigest.getSha512(managerLoginDto.getManagerPw(),
						managerLoginDto.getManagerId().toLowerCase()).equals(u.getManagerPw()))
				.map(u-> QsolModelMapper.map(u, ManagerInfo.class))
				.orElse(null);
	}

	public void usingUpdateTrue(String managerId) { // 로그인에 성공한 매니저 id의 using을 1로 변경
		if(!managerId.equals("root")) { // 최고 관리자는 실행 X
			managerRepository.usingUpdateTrue(managerId);
		}
	}
	public void usingUpdateFalse(String managerId) { // 로그아웃시 manager테이블의 using을 0으로 변경
		if(!managerId.equals("root")) { // 최고 관리자는 실행 X
			managerRepository.usingUpdateFalse(managerId);
		}
	}

	public ListDto<ManagerViewDto> list(SearchDto searchDto) { // 관리자 목록 테이블 가져오기 :list
		Pageable pageable = PageRequest.of(searchDto.getPageNo()-1, searchDto.getCountPerPage());
		List<ManagerViewDto> list = null;
		Long count = 0L;
		List<ManagerInterface> managerList = managerRepository.list(pageable);
		if(managerList != null) {
			list = QsolModelMapper.map(managerList, ManagerViewDto.class);
			count = managerRepository.listCount(searchDto); // 관리자 count 수 가져오기 :paging
		}
		ListDto<ManagerViewDto> result = ListDto.<ManagerViewDto>builder()
				.list(list)
				.count(count)
				.build();
		return result;
	}

	@Transactional // 관리자 신규 등록 :create
	public void create(ManagerViewDto managerViewDto) throws NoSuchAlgorithmException {
		int duplicateCheck = managerRepository.managerIdDuplicateCheck(managerViewDto.getManagerId()); // 중복 확인을 위한
		if(managerViewDto.isLessCharacters(managerViewDto.getManagerId())) { // 아이디 4글자 아래면 에러
			throw new LessCharactersManagerIdException();
		} else if(managerViewDto.isLessCharacters(managerViewDto.getManagerPw()) || managerViewDto.isLessCharacters(managerViewDto.getConfirmPw())) {
			// 비밀번호 or 비밀번호 확인 4글자 아래면 에러
			throw new LessCharactersManagerPwException();
		} else if(duplicateCheck > 0) { // 아이디가 이미 존재한다면 에러
			throw new ManagerIdDuplicateException();
		} else if(!managerViewDto.getManagerPw().equals(managerViewDto.getConfirmPw())) { // 비밀번호와 비밀번호 확인이 일치하지 않으면 에러
			throw new ConfirmPwException();
		} else {
			managerViewDto.setManagerPw(QsolMessageDigest.getSha512(managerViewDto.getManagerPw(), managerViewDto.getManagerId().toLowerCase()));
			//managerViewDto.setUsing(false);
			Manager manager = QsolModelMapper.map(managerViewDto, Manager.class);
			managerRepository.save(manager); // JPA Create
		}
	}

	public ManagerViewDto view(String managerId) { // 관리자 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
		Manager manager = managerRepository.findByManagerId(managerId);
		ManagerViewDto result = null;
		if(manager != null) {
			result = QsolModelMapper.map(manager, ManagerViewDto.class);
		}
		return result;
	}

	@Transactional // 관리자 업데이트 :update
	public void update(ManagerViewDto managerViewDto) throws NoSuchAlgorithmException  {
		Manager manager = QsolModelMapper.map(managerViewDto, Manager.class);
		managerRepository.save(manager);
	}

	@Transactional
    public void delete(String managerId) {
		managerRepository.deleteByManagerId(managerId);
    }
}
