package qsol.qsoljecheonweb.image.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import qsol.qsoljecheonweb.domain.carImg.CarImgInfo;
import qsol.qsoljecheonweb.image.model.response.ImageInfoDto;

@Repository
public interface ImageRepository extends JpaRepository<CarImgInfo, String> {

    @Query(value = "SELECT COUNT(*) FROM tb_car_imginfo c WHERE c.VAR_CD=:varcd AND c.VML_CD=:vmlcd", nativeQuery = true)
    int DuplicateCheck(@Param("varcd") String varcd,@Param("vmlcd") String vmlcd);

    @Query(value = "SELECT c.IMG_NAME AS imgName FROM tb_car_imginfo c WHERE c.VAR_CD=:varcd AND c.VML_CD=:vmlcd", nativeQuery = true)
    String imgCall(@Param("varcd") String varcd,@Param("vmlcd") String vmlcd);

    @Query(value = "DELETE FROM tb_car_imginfo WHERE VAR_CD=:varcd AND VML_CD=:vmlcd", nativeQuery = true)
    void imgDelete(@Param("varcd") String varcd,@Param("vmlcd") String vmlcd);
}

