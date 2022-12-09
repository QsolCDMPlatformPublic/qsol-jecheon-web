package qsol.qsoljecheonweb.diagnosis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qsol.qsoljecheonweb.device.interfaceDevice.DataSaveInfoInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.GuestDataSaveInfoInterface;
import qsol.qsoljecheonweb.device.interfaceDevice.HistoryInterface;
import qsol.qsoljecheonweb.device.model.response.HistoryViewDto;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.TestMasterInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.RealTimeDataInterface;
import qsol.qsoljecheonweb.diagnosis.model.DataSaveInfo;
import qsol.qsoljecheonweb.diagnosis.model.GuestDataSaveInfo;
import qsol.qsoljecheonweb.domain.diagnosis.TestMaster;

import java.util.List;

public interface TestMasterRepository extends JpaRepository<TestMaster, String> {

    @Query(value = "SELECT tm.LOC_ID AS locid, tm.TEST_ID AS testid FROM tb_test_master tm WHERE tm.MANAGER_ID=:managerId ORDER BY tm.REGIST_DT DESC LIMIT 1", nativeQuery = true)
    TestMasterInterface updConnectionForData(@Param("managerId") String managerId);

    // 테스트 시작시간과 현재 SOC, 진단 상태 정보 가져오기
    /*진단 상태 검사를 tb_test_status_history 에서 tb_doha_status_now로 변경
        @Query(value = "SELECT tm.START_DT AS startDt, (SELECT td.SOC FROM tb_test_master tm, tb_test_data td WHERE tm.TEST_ID=td.TEST_ID AND tm.TEST_ID=:testid ORDER BY td.REGIST_DT DESC LIMIT 1) AS soc, (SELECT tsh.STATUS_CD FROM tb_test_master tm, tb_test_status_history tsh WHERE tm.TEST_ID=tsh.TEST_ID AND tm.TEST_ID=:testid ORDER BY tsh.REGIST_DT DESC LIMIT 1) AS statusCd FROM tb_test_master tm WHERE tm.TEST_ID=:testid", nativeQuery = true)*/
    @Query(value = "SELECT tm.START_DT AS startDt,\n" +
            "(SELECT IFNULL(max(td.SOC), 0) FROM tb_test_master tm, tb_test_data td WHERE tm.TEST_ID=td.TEST_ID AND tm.TEST_ID=:testid ORDER BY td.REGIST_DT DESC LIMIT 1) AS soc,\n" +
            "(SELECT IFNULL(max(doha.STEP_CD), 999) FROM tb_test_master tm, tb_doha_status_now doha WHERE tm.TEST_ID=doha.TEST_ID AND tm.TEST_ID=:testid ORDER BY doha.STATUS_DT DESC LIMIT 1) AS stepCd\n" +
            "FROM tb_test_master tm WHERE tm.TEST_ID=:testid\n", nativeQuery = true)
    List<RealTimeDataInterface> startDtAndSocAndStepCd(@Param("testid") int testid);

    @Query(value = "SELECT test_id FROM tb_test_master WHERE loc_id=:locid ORDER BY regist_dt desc LIMIT 1", nativeQuery = true)
    int getTestid(@Param("locid") Long locid);

    @Query(value = "SELECT CASE WHEN m.END_DT IS null THEN 'FALSE' ELSE 'TRUE' END FROM tb_test_master m ORDER BY m.REGIST_DT DESC LIMIT 1" , nativeQuery = true)
    String historyCheck();

    @Query(value = "SELECT m.TEST_ID as testid, m.LOC_ID as locid, m.CUSTOMER_ID as cid FROM tb_test_master m ORDER BY m.REGIST_DT DESC LIMIT 1", nativeQuery = true)
    HistoryInterface historyDataGet();

    @Query(value = "SELECT tm.TEST_ID AS testid, tm.LOC_ID AS locid, tm.CUSTOMER_ID AS cid, c.NAME AS customerName, tc.VEHICLE_NO AS vehicleNo, \n" +
            "tc.VAR_CD AS varcd, tc.VML_CD AS vmlcd, tc.CAR_YEAR AS carYear, IFNULL(tc.MILEAGE, 0) AS mileage, ci.CODENM AS vmlCodeNm, ci.REFERENCE AS varCodeNm\n" +
            "FROM tb_test_master tm, tb_customers c, tb_test_car tc, tb_codeinfo ci \n" +
            "WHERE tm.CUSTOMER_ID=c.ID AND tm.TEST_ID=tc.TEST_ID AND tc.VML_CD=ci.CODE AND tm.LOC_ID=:locid\n" +
            "ORDER BY tm.REGIST_DT DESC LIMIT 1\n", nativeQuery = true)
    DataSaveInfoInterface recallData(@Param("locid") Long locid);

    @Query(value = "SELECT tm.TEST_ID AS testid, tm.LOC_ID AS locid, tm.CUSTOMER_ID AS cid, tc.VEHICLE_NO AS vehicleNo, \n" +
            "CASE WHEN tm.CUSTOMER_ID=0 THEN 'guest' END AS customerName,\n" +
            "tc.VAR_CD AS varcd, tc.VML_CD AS vmlcd, tc.CAR_YEAR AS carYear, IFNULL(tc.MILEAGE, 0) AS mileage, ci.CODENM AS vmlCodeNm, ci.REFERENCE AS varCodeNm\n" +
            "FROM tb_test_master tm, tb_customers c, tb_test_car tc, tb_codeinfo ci \n" +
            "WHERE tm.TEST_ID=tc.TEST_ID AND tc.VML_CD=ci.CODE AND tm.LOC_ID=:locid AND tm.CUSTOMER_ID=0\n" +
            "ORDER BY tm.REGIST_DT DESC LIMIT 1\n", nativeQuery = true)
    GuestDataSaveInfoInterface guestRecallData(@Param("locid") Long locid);

    @Query(value = "SELECT CASE WHEN CUSTOMER_ID=0 THEN 'guest' ELSE 'notGuest' END FROM tb_test_master WHERE LOC_ID=:locid ORDER BY REGIST_DT DESC LIMIT 1", nativeQuery = true)
    String returnForCustomerName(@Param("locid") Long locid);


    @Query(value = "SELECT TEST_ID FROM tb_test_master WHERE LOC_ID=:locid ORDER BY REGIST_DT DESC LIMIT 1", nativeQuery = true)
    Long stopProcedureCallForTestidFind(@Param("locid") Long locid);
}
