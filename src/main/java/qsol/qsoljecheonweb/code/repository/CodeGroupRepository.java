package qsol.qsoljecheonweb.code.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodeInterface;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodegpCallInterpage;
import qsol.qsoljecheonweb.code.model.response.CarCodeViewDto;
import qsol.qsoljecheonweb.domain.code.CodeGroup;

import java.util.List;

@Repository
public interface CodeGroupRepository extends JpaRepository<CodeGroup, String> {

    // 홈화면 시작시 차량 제조사 관련 코드 그룹(셀렉박스) 불러오기 :carCodegpCall
    @Query(value = "SELECT g.codegp as codevalue, CASE WHEN g.codegpnm ='차량제조사' THEN 'vehicleMaker' WHEN g.codegpnm='차량모델' THEN 'vehicleName' END AS codelabel FROM tb_codegroup g WHERE g.codegp = 'var' OR g.codegp = 'vml'", nativeQuery = true)
    List<CarCodegpCallInterpage> carCodegpCall();

    // 차량 모델 생성시 차량 제조사 이름 가져와서 셀렉박스 생성
    @Query(value = "SELECT c.CODENM AS codenm FROM tb_codeinfo c WHERE c.codegp='VAR'", nativeQuery = true)
    List<CarCodeInterface> vehicleMakerList();

    @Query(value = "SELECT c.codenm AS codenm FROM tb_codeinfo c WHERE c.CODE=:customersCarMaker", nativeQuery = true)
    String referenceGet(@Param("customersCarMaker") String customersCarMaker);

}
