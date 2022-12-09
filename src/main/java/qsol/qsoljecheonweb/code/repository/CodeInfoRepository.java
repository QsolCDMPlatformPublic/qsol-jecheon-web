package qsol.qsoljecheonweb.code.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodeInterface;
import qsol.qsoljecheonweb.code.interfaceCode.CarCodegpCallInterpage;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.domain.code.CodeInfo;

import java.util.List;

@Repository
public interface CodeInfoRepository extends JpaRepository<CodeInfo, String> {

    // 차량 제조사+모델 코드 리스트 불러오기 :list
    @Query(value = "SELECT c.code as CODE, c.codenm as codenm, \n" +
            "CASE WHEN c.codegp = 'VAR' THEN '차량제조사' ELSE '차량모델' END AS codegp,\n" +
            "CASE WHEN c.useyn = '1' THEN '사용중' ELSE '사용중지' END AS useynMessage \n" +
            "FROM tb_codeinfo c WHERE c.codegp='VAR' OR c.codegp='VML' ORDER BY c.code", nativeQuery = true)
    List<CarCodeInterface> codeVarAndVmlList(Pageable pageable);
    // 차량 제조사+모델 코드 개수 불러오기 :paging
    @Query(value = "SELECT COUNT(*) FROM tb_codeinfo c WHERE c.codegp='VAR' OR c.codegp='VML'", nativeQuery = true)
    Long codeVarAndVmlCount(SearchDto searchDto);



    // country 코드 리스트 불러오기 :list
    @Query(value = "SELECT c.code as CODE, c.codenm as codenm,\n" +
            "CASE WHEN c.codegp = 'CTR' THEN '국가' END AS codegp,\n" +
            "CASE WHEN c.useyn = '1' THEN '사용중' ELSE '사용중지' END AS useynMessage\n" +
            "FROM tb_codeinfo c WHERE c.codegp='CTR' ORDER BY c.code", nativeQuery = true)
    List<CarCodeInterface> countryCodeList(Pageable pageable);
    // country 코드 개수 불러오기 :paging
    @Query(value = "SELECT COUNT(*) FROM tb_codeinfo c WHERE c.codegp='CTR'", nativeQuery = true)
    Long countryCodeListCount(SearchDto searchDto);




    // 차량 관련 코드 상세정보 팝업 노출을 위해 해당 코드의 데이터를 불러오기 :view
    CodeInfo findByCode(String code);

    // 차량 관련 코드 삭제 :delete
    void deleteByCode(String code);

    // 홈화면 시작시 차량 제조사 & 모델 코드 리스트(셀렉박스) 불러오기 :vehicleCodeList :getCodeSelectBox
    @Query(value = "SELECT c.code as codevalue, c.codenm AS codelabel FROM tb_codeinfo c WHERE c.codegp = 'var' ORDER BY c.code", nativeQuery = true)
    List<CarCodegpCallInterpage> varCodeListCall();
    /*@Query(value = "SELECT c.code as codevalue, c.codenm AS codelabel FROM tb_codeinfo c WHERE c.codegp = 'vml' ORDER BY c.code", nativeQuery = true)
    List<CarCodegpCallInterpage> vmlCodeListCall();*/
    @Query(value = "SELECT c.code as codevalue, c.codenm AS codelabel FROM tb_codeinfo c WHERE c.codegp = 'ctr' ORDER BY c.code", nativeQuery = true)
    List<CarCodegpCallInterpage> ctrCodeListCall();

    // 차량 신규 코드 생성 시 코드 자동 생성을 위해
    @Query(value = "SELECT right(c.CODE, 4) AS codeNumber FROM tb_codeinfo c WHERE c.CODEGP=:codegp ORDER BY c.CODE DESC LIMIT 1", nativeQuery = true)
    String codeNumberForCreate(@Param("codegp") String codegp);

    // 선택한 차량 제조사에 해당하는 차량 모델들을 가져옴
    @Query(value = "SELECT c.code as codevalue, c.codenm AS codelabel FROM tb_codeinfo c WHERE c.reference=:reference ORDER BY c.code", nativeQuery = true)
    List<CarCodegpCallInterpage> customerCarNameSelectBox(@Param("reference") String reference);

    @Query(value = "SELECT codenm FROM tb_codeinfo WHERE CODE=:item", nativeQuery = true)
    String getCodeNm(@Param("item") String item);

}
