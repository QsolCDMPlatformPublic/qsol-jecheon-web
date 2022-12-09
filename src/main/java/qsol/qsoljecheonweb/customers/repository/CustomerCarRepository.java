package qsol.qsoljecheonweb.customers.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.customers.interfaceCustomer.CustomersInterface;
import qsol.qsoljecheonweb.customers.model.response.CustomerCarViewDto;
import qsol.qsoljecheonweb.domain.customers.CustomerCar;

import java.util.List;

@Repository
public interface CustomerCarRepository extends JpaRepository<CustomerCar, String> {

    // 고객 아이디를 기준으로 등록된 차량 리스트 가져오기 :vehicleList
    @Query(value = "SELECT\n" +
            "cc.ID AS carId, cc.CUSTOMER_ID AS customerId, cc.VEHICLE_NO AS customersCarNumber, cc.CAR_YEAR AS customersCarYear, /*IFNULL(MAX(cc.MILEAGE), 0)*/cc.MILEAGE AS customersCarMileage, cc.REGIST_DT AS registDt,\n" +
            "(SELECT ci.CODENM FROM tb_customer_car cc, tb_codeinfo ci WHERE ci.CODE=cc.VAR_CD AND cc.CUSTOMER_ID=:cid AND cc.VEHICLE_NO=customersCarNumber) AS customersCarMaker,\n" +
            "(SELECT ci.CODENM FROM tb_customer_car cc, tb_codeinfo ci WHERE ci.CODE=cc.VML_CD AND cc.CUSTOMER_ID=:cid AND cc.VEHICLE_NO=customersCarNumber) AS customersCarName\n" +
            "FROM tb_customer_car cc\n" +
            "WHERE cc.customer_id=:cid ORDER BY cc.VEHICLE_NO", nativeQuery = true)
    List<CustomersInterface> findByCustomerId(@Param("cid") Long cid, Pageable pageable);
    // 고객 아이디를 기준으로 등록된 차량 리스트 가져오기 :paging
    @Query(value = "SELECT count(*) FROM tb_customer_car cc WHERE cc.customer_id=:cid", nativeQuery = true)
    Long vehicleListCount(@Param("cid") Long cid);

    // 차량 상세정보 표시를 위해 데이터 가져오기 :carView
    @Query(value = "SELECT \n" +
            "cc.ID as carId, cc.CUSTOMER_ID AS customerId, cc.VEHICLE_NO AS customersCarNumber, cc.VAR_CD AS customersCarMaker, cc.VML_CD AS customersCarName, cc.CAR_YEAR AS customersCarYear, IFNULL(MAX(cc.MILEAGE), 0) AS customersCarMileage, cc.REGIST_DT as registDt\n" +
            "FROM tb_customer_car cc WHERE cc.ID=:carId", nativeQuery = true)
    List<CustomersInterface> findByCustomersCarNumber(@Param("carId") Long carId);

    // 더티 체킹을 위해 차량 번호와 고객 아이디를 기준으로 데이터를 가져옴 :carUpdate
    @Query(value = "SELECT * FROM tb_customer_car cc WHERE cc.ID=:carId", nativeQuery = true)
    CustomerCar carUpdateForSelect(@Param("carId") Long carId);

    // 진단을 위한 고객 차량 선택 시 해당 carId를 기준으로 해당 차량의 정보를 세션에 저장
    @Query(value = "SELECT cc.ID as carId, cc.CUSTOMER_ID AS customerId, cc.VEHICLE_NO AS customersCarNumber, cc.VAR_CD AS customersCarMaker, cc.VML_CD AS customersCarName, cc.CAR_YEAR AS customersCarYear, IFNULL(MAX(cc.MILEAGE), 0) AS customersCarMileage\n" +
            "FROM tb_customer_car cc WHERE cc.ID=:carId", nativeQuery = true)
    List<CustomersInterface> findAllForSessionSave(@Param("carId") Long carId);

    // Diagnosis Start 에서 주행거리 업데이트 버튼 실행
    @Query(value = "UPDATE tb_customer_car cc SET cc.MILEAGE=:mileage WHERE cc.CUSTOMER_ID=:cid AND cc.VEHICLE_NO=:vehicleNo", nativeQuery = true)
    void mileageChange(@Param("cid") Long cid,@Param("mileage") Long mileage,@Param("vehicleNo") String vehicleNo);

    // 고객 삭제 시 해당 고객의 차량을 삭제
    @Query(value = "DELETE FROM tb_customer_car WHERE CUSTOMER_ID=:cid", nativeQuery = true)
    void deleteByCustomerId(@Param("cid") Long cid);

    void deleteById(Long carId);
}

