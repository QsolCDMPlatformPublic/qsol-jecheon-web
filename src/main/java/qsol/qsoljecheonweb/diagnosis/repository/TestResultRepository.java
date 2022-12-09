package qsol.qsoljecheonweb.diagnosis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.DiagnosisInterface;
import qsol.qsoljecheonweb.diagnosis.interfaceDiagnosis.ReportDataInterface;
import qsol.qsoljecheonweb.domain.diagnosis.TestResult;
import qsol.qsoljecheonweb.result.interfaceResult.ResultInterface;
import qsol.qsoljecheonweb.result.interfaceResult.ResultViewInterface;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, String> {
    // customers list 고객 진단 결과 팝업 리스트
    @Query(value = "SELECT \n" +
            "tr.TEST_ID AS testid, tr.ELAPSED_SECONDS AS elapsedSeconds, tr.GRADE AS grade, tr.LAST_SOC AS lastSoc, tr.SOH AS soh, tr.REASON_MSG AS reasonMsg,\n" +
            "tm.START_DT AS startDT, tm.END_DT AS endDt, tm.MANAGER_ID AS managerId,\n" +
            "tc.VEHICLE_NO AS vehicleNo, tc.CAR_YEAR AS carYear,\n" +
            "(SELECT c.CODENM FROM tb_test_master tm, tb_test_car tc, tb_codeinfo c WHERE tm.TEST_ID=tc.TEST_ID AND tc.VAR_CD=c.CODE AND tc.TEST_ID=testid) AS varcd,\n" +
            "(SELECT c.CODENM FROM tb_test_master tm, tb_test_car tc, tb_codeinfo c WHERE tm.TEST_ID=tc.TEST_ID AND tc.VML_CD=c.CODE AND tc.TEST_ID=testid) AS vmlcd\n" +
            "FROM tb_test_result tr, tb_test_master tm, tb_test_car tc\n" +
            "WHERE tm.TEST_ID=tr.TEST_ID AND tr.TEST_ID=tc.TEST_ID AND tm.CUSTOMER_ID=:cid", nativeQuery = true)
    List<DiagnosisInterface> diagnosisResultList(@Param("cid") Long cid, Pageable pageable);
    // customers list 고객 진단 결과 팝업 리스트 수 (count)
    @Query(value = "SELECT COUNT(*) FROM tb_test_result tr, tb_test_master tm, tb_test_car tc WHERE tm.TEST_ID=tr.TEST_ID AND tr.TEST_ID=tc.TEST_ID AND tm.CUSTOMER_ID=:cid", nativeQuery = true)
    Long diagnosisResultListCount(@Param("cid") Long cid);

    // 테스트 시작 프로시저 호출
    @Query(value = "CALL sp_test_start(:locid,:customerId,:vehicleNo,:varcd,:vmlcd,:carYear,:mileage,:managerId,@test_id);", nativeQuery = true)
    void testStart(@Param("locid") Long locid,@Param("customerId") Long customerId,@Param("vehicleNo") String vehicleNo,@Param("varcd") String varcd,@Param("vmlcd") String vmlcd,@Param("carYear") String carYear,@Param("mileage") Long mileage,@Param("managerId") String managerId);

    @Query(value = "CALL sp_test_start(:locid,:customerId,:vehicleNo,:varcd,:vmlcd,:carYear,:mileage,:managerId,@test_id);", nativeQuery = true)
    void guestTestStart(@Param("locid") Long locid,@Param("customerId") Long customerId,@Param("vehicleNo") String vehicleNo,@Param("varcd") String varcd,@Param("vmlcd") String vmlcd,@Param("carYear") Long carYear,@Param("mileage") Long mileage,@Param("managerId") String managerId);

    @Query(value = "SELECT grade AS grade, last_soc AS soc, soh AS soh FROM tb_test_result WHERE test_id=:testid", nativeQuery = true)
    ReportDataInterface report(@Param("testid") int testid);




// ┌──────────────────────────────────────────── result List Search ─────────────────────────────────────────────────────────────────┐
// │
    String SelectQuery = "SELECT tr.TEST_ID AS testid, tr.SOH AS soh, tr.REGIST_DT AS registDt, tc.VEHICLE_NO AS carNumber, " +
            "ci.REFERENCE AS carMaker, ci.CODENM AS carName, tm.CUSTOMER_ID AS customerId, c.NAME AS customerName, tm.EVCCID AS macAddress ";
    String FromQuery = " FROM tb_test_result tr, tb_test_car tc, tb_codeinfo ci, tb_test_master tm, tb_customers c ";
    String PreWhereQuery = "WHERE tr.TEST_ID=tc.TEST_ID AND tc.VML_CD=ci.CODE AND tm.TEST_ID=tr.TEST_ID AND c.ID=tm.CUSTOMER_ID ";
    String OrderByQuery = " ORDER BY tr.REGIST_DT DESC, c.NAME";

    // 모든 정보
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + OrderByQuery, nativeQuery = true)
    List<ResultInterface> list(Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + OrderByQuery, nativeQuery = true)
    Long listCount(CustomerSearchDto customerSearchDto);

    // 조건(이름) 검색
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName " + OrderByQuery, nativeQuery = true)
    List<ResultInterface> searchNameList(@Param("searchName") String searchName, Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName " + OrderByQuery, nativeQuery = true)
    Long searchNameListCount(@Param("searchName") String searchName);

    // 조건(이름+제조사) 검색
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName AND tc.VAR_CD=:searchMaker " + OrderByQuery, nativeQuery = true)
    List<ResultInterface> searchNameMakerList(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName AND tc.VAR_CD=:searchMaker " + OrderByQuery, nativeQuery = true)
    Long searchNameMakerListCount(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker);

    // 조건(제조사) 검색
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + " AND tc.VAR_CD=:searchMaker " + OrderByQuery, nativeQuery = true)
    List<ResultInterface> searchMakerList(@Param("searchMaker")String searchMaker, Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + " AND tc.VAR_CD=:searchMaker " + OrderByQuery, nativeQuery = true)
    Long searchMakerListCount(@Param("searchMaker")String searchMaker);

    // 조건(제조사+모델) 검색
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + " AND tc.VAR_CD=:searchMaker AND tc.VML_CD=:searchModel " + OrderByQuery, nativeQuery = true)
    List<ResultInterface> searchMakerModelList(@Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel, Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + " AND tc.VAR_CD=:searchMaker AND tc.VML_CD=:searchModel " + OrderByQuery, nativeQuery = true)
    Long searchMakerModelListCount(@Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel);

    // 조건(이름+제조사+모델) 검색
    @Query(value = SelectQuery + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName AND tc.VAR_CD=:searchMaker AND tc.VML_CD=:searchModel " + OrderByQuery, nativeQuery = true)
    List<ResultInterface> searchAllList(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel, Pageable pageable);
    @Query(value = "SELECT COUNT(*) " + FromQuery + PreWhereQuery + " AND c.NAME LIKE :searchName AND tc.VAR_CD=:searchMaker AND tc.VML_CD=:searchModel " + OrderByQuery, nativeQuery = true)
    Long searchAllListCount(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel);
// │
// └──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

    @Query(value = "SELECT tr.GRADE AS grade, tr.SOH AS soh, tc.VEHICLE_NO AS carNumber, tc.VAR_CD AS varcd, tc.VML_CD AS vmlcd, " +
            "tc.CAR_YEAR AS carYear, ci.REFERENCE AS varCodeNm, ci.CODENM AS vmlCodeNm, IFNULL(tc.MILEAGE, 0) as mileage\n" +
            "FROM tb_test_result tr, tb_test_car tc, tb_codeinfo ci WHERE tr.TEST_ID=tc.TEST_ID AND tc.VML_CD=ci.CODE AND tr.TEST_ID=:testid", nativeQuery = true)
    ResultViewInterface result(@Param("testid") int testid);

    @Query(value = "CALL sp_test_end(:testid, :reqid, :saveDirectory, :fileName, :size, :elapsedSeconds, :grade, :lastSoc, :soh, :errorYn, :reasonMsg)", nativeQuery = true)
    void stopProcedureCall(@Param("testid") Long testid,@Param("reqid") int reqid,@Param("saveDirectory") String saveDirectory,@Param("fileName") String fileName,@Param("size") int size,@Param("elapsedSeconds") int elapsedSeconds,@Param("grade") String grade,@Param("lastSoc") int lastSoc,@Param("soh") int soh,@Param("errorYn") int errorYn,@Param("reasonMsg") String reasonMsg);
}
