package qsol.qsoljecheonweb.device.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.device.interfaceDevice.DeviceInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.DeviceStaffInterface;
import qsol.qsoljecheonweb.domain.customers.Customers;
import qsol.qsoljecheonweb.domain.device.GwRegistInfo;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<GwRegistInfo, String> {

    // 진단기 테이블 :list
    @Query(value = "SELECT r.LOC_ID AS locid, r.FIRM_VER AS firmver, r.IP_ADDR AS ipAddress, r.MAC_ADDR AS macAddress, r.DETAIL_DESC AS detail, \n" +
            "LEFT(r.DETAIL_DESC, 10) AS detailAbb, s.MANAGER_ID AS managerId, \n" +
            "CASE\n" +
            "WHEN s.STATUS_CD = 0 THEN 'Standby'\n" +
            "WHEN s.STATUS_CD = 1 THEN 'Connected'\n" +
            "ELSE 'Testing' END AS status,\n" +
            "CASE WHEN s.CONNECT = 0 THEN 'Connect' ELSE 'Disconn' END AS action,\n" +
            "r.REGIST_DT AS registDt\n" +
            "FROM tb_gw_regist_info r, tb_gw_status_info s WHERE r.LOC_ID=s.LOC_ID", nativeQuery = true)
    List<DeviceInterface> list(Pageable pageable);

    @Query(value = "SELECT r.LOC_ID AS locid, r.MAC_ADDR AS macAddress, s.MANAGER_ID AS managerId, \n" +
            "CASE WHEN s.STATUS_CD = 0 THEN 'Standby' WHEN s.STATUS_CD = 1 THEN 'Connected' ELSE 'Testing' END AS status,\n" +
            "CASE WHEN s.CONNECT = 0 THEN 'Connect' ELSE 'Disconn' END AS action \n" +
            "FROM tb_gw_regist_info r, tb_gw_status_info s WHERE r.LOC_ID=s.LOC_ID ORDER BY s.REGIST_DT desc limit 10", nativeQuery = true)
    List<DeviceStaffInterface> staffList();

    // 진단기 테이블 수 :paging
    @Query(value = "SELECT COUNT(*) FROM tb_gw_regist_info r, tb_gw_status_info s WHERE r.LOC_ID=s.LOC_ID", nativeQuery = true)
    Long listCount(SearchDto searchDto);

    // 어떤 진단기를 연결했는지 화면 상단에 보여주기 위해 맥주소 뒤에서 두자리를 가져옴
    @Query(value = "SELECT RIGHT(g.MAC_ADDR, 2) FROM tb_gw_regist_info g WHERE g.LOC_ID=:locid", nativeQuery = true)
    String findMacAddress(@Param("locid") Long locid);
}

