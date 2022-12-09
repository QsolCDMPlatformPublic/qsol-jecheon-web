package qsol.qsoljecheonweb.customers.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.request.CustomerSearchDto;
import qsol.qsoljecheonweb.device.interfaceDevice.DataSaveInfoInterface;
import qsol.qsoljecheonweb.diagnosis.model.DataSaveInfo;
import qsol.qsoljecheonweb.domain.customers.Customers;

import java.util.List;

@Repository
public interface CustomersRepository extends JpaRepository<Customers, String> {

    // 고객 중복 검사를 위해 이름 및 휴대폰 검사
    @Query(value = "SELECT COUNT(*) FROM tb_customers c WHERE c.name=:customersName AND c.phone=:customersPhone", nativeQuery = true)
    int customerDuplicateCheck(@Param("customersName") String customersName, @Param("customersPhone") String customersPhone);
    // 차량 중복 검사를 위해 차량 번호 검사
    @Query(value = "SELECT COUNT(*) FROM tb_customer_car cc WHERE cc.vehicle_no=:customersCarNumber", nativeQuery = true)
    int vehicleDuplicateCheck(@Param("customersCarNumber") String customersCarNumber);


// ┌──────────────────────────────────────────── customers List Search ─────────────────────────────────────────────────────────────────┐
// │
    String SelectQuery = " c.id AS cid, c.name AS customersName, \n" +
        "IFNULL(ci.REFERENCE, \"\") AS vehicleMaker, \n" +
        "IFNULL(ci.CODENM, \"\") AS vehicleName,\n" +
        "(SELECT COUNT(tc.VML_CD) FROM tb_test_car tc, tb_customers c, tb_customer_car cc, tb_test_master tm\n" +
        "WHERE tc.TEST_ID=tm.TEST_ID AND tm.CUSTOMER_ID=cc.CUSTOMER_ID AND cc.CUSTOMER_ID=cid AND c.name=customersName AND tc.VML_CD=ci.code AND cc.CAR_YEAR=tc.CAR_YEAR AND cc.VEHICLE_NO=tc.VEHICLE_NO) AS testCount,\n" +
        "c.phone AS customersPhone, c.address1 AS customersAddress1, c.address2 AS customersAddress2, c.zipcode AS customersZipcode,\n" +
        "(SELECT ci.codenm FROM tb_codeinfo ci WHERE ci.code=c.country) AS customersCountry,\n" +
        "CONCAT(c.zipcode, ' ', c.address1, ' ', c.address2, ' ', (SELECT ci.codenm FROM tb_codeinfo ci WHERE ci.code=c.country)) AS customersShowAddress,\n" +
        "c.REGIST_DT AS customersRegistDt ";
    String FromQuery = "FROM tb_customers c \n" +
        "LEFT JOIN tb_customer_car cc ON cc.customer_id=c.id \n" +
        "LEFT JOIN tb_codeinfo ci ON cc.VML_CD=ci.CODE ";

    // 모든 고객 테이블 :list
    @Query(value = "SELECT" + SelectQuery + FromQuery + "ORDER BY c.regist_dt DESC", nativeQuery = true)
    List<CustomersInterface> list(Pageable pageable);
    // 모든 고객 테이블 수 :paging
    @Query(value = "SELECT count(*) " + FromQuery + "ORDER BY c.regist_dt DESC", nativeQuery = true)
    Long listCount(CustomerSearchDto customerSearchDto);

    // 조건(이름) 검색
    @Query(value = "SELECT" + SelectQuery + FromQuery + " WHERE c.name like :searchName ORDER BY c.regist_dt desc ", nativeQuery = true)
    List<CustomersInterface> searchNameList(@Param("searchName") String searchName, Pageable pageable);
    @Query(value = "SELECT count(*) " + FromQuery + "WHERE c.name=:searchName", nativeQuery = true)
    Long searchNameListCount(@Param("searchName") String searchName);

    // 조건(이름+제조사) 검색
    @Query(value = "SELECT DISTINCT" + SelectQuery + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND c.name LIKE :searchName AND cc.VAR_CD=:searchMaker ORDER BY c.regist_dt DESC", nativeQuery = true)
    List<CustomersInterface> searchNameMakerList(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, Pageable pageable);
    @Query(value = "SELECT COUNT(DISTINCT c.name) " + FromQuery +
            " WHERE c.id=cc.CUSTOMER_ID AND c.name LIKE :searchName AND cc.VAR_CD=:searchMaker ORDER BY c.regist_dt DESC", nativeQuery = true)
    Long searchNameMakerListCount(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker);

    // 조건(제조사) 검색
    @Query(value = "SELECT DISTINCT" + SelectQuery + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND cc.VAR_CD=:searchMaker ORDER BY c.regist_dt DESC", nativeQuery = true)
    List<CustomersInterface> searchMakerList(@Param("searchMaker")String searchMaker, Pageable pageable);
    @Query(value = "SELECT COUNT(DISTINCT c.name) " + FromQuery + "WHERE c.id=cc.CUSTOMER_ID AND cc.VAR_CD=:searchMaker ORDER BY c.regist_dt DESC", nativeQuery = true)
    Long searchMakerListCount(@Param("searchMaker")String searchMaker);

    // 조건(제조사+모델) 검색
    @Query(value = "SELECT DISTINCT" + SelectQuery + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND cc.VAR_CD=:searchMaker AND cc.VML_CD=:searchModel ORDER BY c.regist_dt DESC", nativeQuery = true)
    List<CustomersInterface> searchMakerModelList(@Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel, Pageable pageable);
    @Query(value = "SELECT COUNT(DISTINCT c.name)\n" + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND cc.VAR_CD=:searchMaker AND cc.VML_CD=:searchModel ORDER BY c.regist_dt DESC", nativeQuery = true)
    Long searchMakerModelListCount(@Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel);

    // 조건(이름+제조사+모델) 검색
    @Query(value = "SELECT DISTINCT" + SelectQuery + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND c.name LIKE :searchName AND cc.VAR_CD=:searchMaker AND cc.VML_CD=:searchModel ORDER BY c.regist_dt DESC", nativeQuery = true)
    List<CustomersInterface> searchAllList(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel, Pageable pageable);
    @Query(value = "SELECT COUNT(DISTINCT c.name)\n" + FromQuery + " WHERE c.id=cc.CUSTOMER_ID AND c.name LIKE :searchName AND cc.VAR_CD=:searchMaker AND cc.VML_CD=:searchModel ORDER BY c.regist_dt DESC", nativeQuery = true)
    Long searchAllListCount(@Param("searchName")String searchName, @Param("searchMaker")String searchMaker, @Param("searchModel")String searchModel);
// │
// └──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘
    
    
    // 고객 상세정보 표시를 위해 데이터 가져오기 :view
    @Query(value = "SELECT \n" +
            "c.id AS cid, c.name AS customersName, c.phone AS customersPhone, c.address1 AS customersAddress1, c.address2 AS customersAddress2, c.zipcode AS customersZipcode, c.country AS customersCountry \n" +
            "FROM tb_customers c WHERE c.ID=:cid", nativeQuery = true)
    List<CustomersInterface> findByCid(@Param("cid") Long cid);


    @Query(value = "SELECT * FROM tb_customers c WHERE c.ID=:cid", nativeQuery = true)
    Customers updateForSelect(@Param("cid") Long cid);

    @Query(value = "SELECT NAME FROM tb_customers WHERE id=:cid", nativeQuery = true)
    String customerName(@Param("cid") Long cid);


// ┌──────────────────────────────────────────── diagnosticCustomerInfo ─────────────────────────────────────────────────────────────────┐
// │ 진단이 가능한 고객의 (+차량) 정보 테이블 가져오기

    String SelectQuery2 = " c.id AS cid, c.name AS customersName, c.phone AS customersPhone, c.address1 AS customersAddress1, c.address2 AS customersAddress2, c.zipcode AS customersZipcode,\n" +
            "(SELECT ci.codenm FROM tb_codeinfo ci WHERE ci.code=c.country) AS customersCountry,\n" +
            "(select COUNT(*) FROM tb_customer_car cc WHERE cc.CUSTOMER_ID=c.id) AS customersVehicleCount,\n" +
            "CONCAT(c.zipcode, ' ', c.address1, ' ', c.address2, ' ', (SELECT ci.codenm FROM tb_codeinfo ci WHERE ci.code=c.country)) AS customersShowAddress,\n" +
            "(SELECT COUNT(*) FROM tb_test_master tm WHERE tm.CUSTOMER_ID=c.id) AS customersTestResultCount,\n" +
            "c.REGIST_DT AS customersRegistDt,\n" +
            "cc.ID AS carId, cc.VEHICLE_NO AS customersCarNumber, cc.CAR_YEAR AS customersCarYear, cc.MILEAGE AS customersCarMileage,\n" +
            "(SELECT ci.CODENM FROM tb_customer_car cc, tb_codeinfo ci WHERE ci.CODE=cc.VAR_CD AND cc.CUSTOMER_ID=c.id AND cc.VEHICLE_NO=customersCarNumber) AS customersCarMaker,\n" +
            "(SELECT ci.CODENM FROM tb_customer_car cc, tb_codeinfo ci WHERE ci.CODE=cc.VML_CD AND cc.CUSTOMER_ID=c.id AND cc.VEHICLE_NO=customersCarNumber) AS customersCarName ";

    // 모든 고객
    @Query(value = "SELECT " + SelectQuery2 + "FROM tb_customers c, tb_customer_car cc WHERE cc.customer_id=c.id order by c.name", nativeQuery = true)
    List<CustomersInterface> diagnosticCustomerInfo(Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM tb_customers c, tb_customer_car cc WHERE cc.customer_id=c.id", nativeQuery = true)
    Long diagnosticCustomerInfoCount(CustomerSearchDto customerSearchDto);

    // 이름 검색
    @Query(value = "SELECT " + SelectQuery2 + "FROM tb_customers c, tb_customer_car cc WHERE cc.customer_id=c.id AND c.name LIKE :searchName order by c.name", nativeQuery = true)
    List<CustomersInterface> diagnosticCustomerInfoSearchName(@Param("searchName")String searchName, Pageable pageable);
    @Query(value = "SELECT COUNT(*) FROM tb_customers c, tb_customer_car cc WHERE cc.customer_id=c.id AND c.name LIKE :searchName", nativeQuery = true)
    Long diagnosticCustomerInfoCountSearchName(@Param("searchName")String searchName);

// │
// └──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

    // 고객 삭제
    /*@Query(value = "DELETE tb_customers WHERE ID=:cid",nativeQuery = true)*/
    void deleteById(Long cid);

    @Query(value = "SELECT c.ID as cid, c.NAME AS customerName, cc.VEHICLE_NO AS vehicleNo, cc.VAR_CD AS varcd, cc.VML_CD AS vmlcd, cc.CAR_YEAR AS carYear, cc.MILEAGE AS mileage FROM tb_customers c, tb_customer_car cc WHERE c.ID=cc.CUSTOMER_ID AND c.ID=:cid", nativeQuery = true)
    DataSaveInfoInterface historyCheck(@Param("cid") Long cid);
}

