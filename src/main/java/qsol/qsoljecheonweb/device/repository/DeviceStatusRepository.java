package qsol.qsoljecheonweb.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qsol.qsoljecheonweb.domain.device.GwStatusInfo;

public interface DeviceStatusRepository extends JpaRepository<GwStatusInfo, String> {

    // 진단을 위한 진단기 선택 시 해당 진단기 상태 변경
    @Query(value = "UPDATE tb_gw_status_info s SET s.STATUS_CD=1, s.CONNECT=1, s.MANAGER_ID=:managerId WHERE s.LOC_ID=:locid", nativeQuery = true)
    void statusChange(@Param("locid") Long locid, @Param("managerId") String managerId);

    // 진단을 위한 진단기 선택 후 고객 테이블 생성 실패 시 진단기 상태 리셋
    @Query(value = "UPDATE tb_gw_status_info s SET s.STATUS_CD=0, s.CONNECT=0, s.MANAGER_ID='' WHERE s.LOC_ID=:locid", nativeQuery = true)
    void statusReset(@Param("locid") Long locid);

    @Query(value = "UPDATE tb_gw_status_info s SET s.STATUS_CD=2 WHERE s.LOC_ID=:locid", nativeQuery = true)
    void statusTestingChange(@Param("locid") Long locid);

    @Query(value = "UPDATE tb_gw_status_info s SET s.CONNECT=0 WHERE s.LOC_ID=:locid", nativeQuery = true)
    void onlyConnectReset(@Param("locid") Long locid);

    @Query(value = "UPDATE tb_gw_status_info s SET s.CONNECT=1 WHERE s.LOC_ID=:locid", nativeQuery = true)
    void onlyConnecting(@Param("locid") Long locid);

    @Query(value = "SELECT s.status_cd FROM tb_gw_status_info s WHERE s.loc_id=:locid", nativeQuery = true)
    int getStats(@Param("locid") Long locid);

    @Query(value = "SELECT s.MANAGER_ID FROM tb_gw_status_info s WHERE s.LOC_ID=:locid", nativeQuery = true)
    String getManagerId(@Param("locid") Long locid);

    @Query(value = "UPDATE tb_gw_status_info s SET s.STATUS_CD=1 WHERE s.LOC_ID=:locid", nativeQuery = true)
    void testStop(@Param("locid") Long locid);

    // 로그아웃되면 현재 진행중이던 진단과 진단기와의 연결을 모두 해제
    @Query(value = "UPDATE tb_gw_status_info s SET s.MANAGER_ID='', s.STATUS_CD=0, s.CONNECT=0 WHERE s.MANAGER_ID=:managerId", nativeQuery = true)
    void disconnection(@Param("managerId") String managerId);

    @Query(value = "SELECT RIGHT(r.MAC_ADDR, 2) FROM tb_gw_regist_info r, tb_gw_status_info s WHERE r.LOC_ID=s.LOC_ID AND s.MANAGER_ID=:managerId", nativeQuery = true)
    String testStartPageConnectDevice(@Param("managerId") String managerId);

    @Query(value = "UPDATE tb_gw_status_info s SET s.MANAGER_ID='manager', s.STATUS_CD=1, s.CONNECT=1 WHERE s.LOC_ID=:locid", nativeQuery = true)
    void resetConnection(@Param("locid") Long locid);

    @Query(value = "SELECT CASE WHEN max(s.MANAGER_ID) IS null THEN 'none' ELSE RIGHT(r.MAC_ADDR, 2) END " +
            "FROM tb_gw_status_info s, tb_gw_regist_info r WHERE s.LOC_ID=r.LOC_ID AND s.MANAGER_ID=:managerId", nativeQuery = true)
    String nowConnectDevice(@Param("managerId") String managerId);

    @Query(value = "SELECT IFNULL(MAX(MANAGER_ID), 'none') FROM tb_gw_status_info", nativeQuery = true)
    String connectManagerId();
}
