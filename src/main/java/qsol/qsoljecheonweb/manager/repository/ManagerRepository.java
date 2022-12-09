package qsol.qsoljecheonweb.manager.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.domain.manager.Manager;
import qsol.qsoljecheonweb.manager.interfaceManager.ManagerInterface;

import java.util.List;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {
    @Query(value = "SELECT IFNULL(MAX(MANAGER_ID), 'NonExistentManagerId') FROM tb_manager WHERE MANAGER_ID=:managerId", nativeQuery = true)
    String managerIdCheck(@Param("managerId") String managerId);

    // 관리자 목록 테이블 데이터 가져오기 :list
    @Query(value = "SELECT m.manager_id AS managerId, m.manager_nm AS managerNm, m.manager_phone AS managerPhone, m.manager_tel AS managerTel, m.manager_email AS managerEmail,\n" +
            "CASE WHEN m.USING = 1 THEN '사용중' ELSE '대기중' END AS usingStatus\n" +
            "FROM tb_manager m WHERE m.manager_id!='root' ORDER BY m.manager_id, m.regist_dt", nativeQuery = true)
    List<ManagerInterface> list(Pageable pageable);
    // 관리자 목록 테이블 데이터 개수 :paging
    @Query(value = "SELECT COUNT(*) FROM tb_manager m", nativeQuery = true)
    Long listCount(SearchDto searchDto);

    // 관리자 아이디 중복 체크를 위한 준비 :create
    @Query(value = "SELECT count(*) FROM tb_manager m WHERE m.manager_id=:managerId", nativeQuery = true)
    int managerIdDuplicateCheck(@Param("managerId") String managerId);

    // 관리자 상세정보 팝업에 데이터 표시를 위해 클릭한 아이디의 데이터 가져오기 :view
    Manager findByManagerId(String managerId);

    // 다른 기기에서 이미 로그인 되어있는지 체크
    @Query(value = "SELECT m.using FROM tb_manager m WHERE m.manager_id=:managerId", nativeQuery = true)
    Boolean managerUsingCheck(@Param("managerId") String managerId);

    // 로그인 시 해당 매니저의 using 1로 변경
    @Query(value = "UPDATE tb_manager m SET m.USING=1 WHERE m.manager_id=:managerId", nativeQuery = true)
    void usingUpdateTrue(@Param("managerId") String managerId);
    // 로그아웃 시 using 0으로 변경
    @Query(value = "UPDATE tb_manager m SET m.USING=0 WHERE m.manager_id=:managerId", nativeQuery = true)
    void usingUpdateFalse(@Param("managerId") String managerId);

    void deleteByManagerId(String managerId);
}

