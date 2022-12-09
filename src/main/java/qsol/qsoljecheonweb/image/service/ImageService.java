package qsol.qsoljecheonweb.image.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import qsol.qsoljecheonweb.common.model.request.SearchDto;
import qsol.qsoljecheonweb.common.model.response.ListDto;
import qsol.qsoljecheonweb.customers.model.response.CustomersViewDto;
import qsol.qsoljecheonweb.domain.carImg.CarImgInfo;
import qsol.qsoljecheonweb.domain.customers.Customers;
import qsol.qsoljecheonweb.domain.manager.Manager;
import qsol.qsoljecheonweb.error.customers.CustomersDuplicateException;
import qsol.qsoljecheonweb.error.customers.LessCharactersCustomersException;
import qsol.qsoljecheonweb.error.image.ImgSaveCodeNullException;
import qsol.qsoljecheonweb.error.image.ImgSaveDuplicateException;
import qsol.qsoljecheonweb.error.image.ImgSaveImgNameNullException;
import qsol.qsoljecheonweb.error.manager.*;
import qsol.qsoljecheonweb.image.model.response.ImageInfoDto;
import qsol.qsoljecheonweb.image.repository.ImageRepository;
import qsol.qsoljecheonweb.manager.interfaceManager.ManagerInterface;
import qsol.qsoljecheonweb.manager.model.ManagerInfo;
import qsol.qsoljecheonweb.manager.model.request.ManagerLoginDto;
import qsol.qsoljecheonweb.manager.model.response.ManagerViewDto;
import qsol.qsoljecheonweb.manager.repository.ManagerRepository;
import qsol.qsoljecheonweb.util.QsolMessageDigest;
import qsol.qsoljecheonweb.util.QsolModelMapper;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Slf4j
@Service
public class ImageService {

    @Value("${file.image.directory}")
    private String fileImageDirectory;

    @Autowired
    private ImageRepository imageRepository;

    @Transactional
    public void imgDelete(ImageInfoDto imageInfoDto) {
        imageRepository.imgDelete(imageInfoDto.getVarcd(), imageInfoDto.getVmlcd()); // JPA Delete
    }

    public void fileUpload(MultipartFile multipartFile) {
        // File.seperator 는 OS종속적이다.
        // Spring에서 제공하는 cleanPath()를 통해서 ../ 내부 점들에 대해서 사용을 억제한다
        Path copyOfLocation = Paths.get(fileImageDirectory + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try {
            // inputStream을 가져와서
            // copyOfLocation (저장위치)로 파일을 쓴다.
            // copy의 옵션은 기존에 존재하면 REPLACE(대체한다), 오버라이딩 한다
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new FileStorageException("Could not store file : " + multipartFile.getOriginalFilename());
        }

    }

    @Transactional // 차량 이미지 테이블에 정보 저장
    public void create(ImageInfoDto imageInfoDto) throws NoSuchAlgorithmException {
        if(imageInfoDto.getVarcd() == null || imageInfoDto.getVmlcd() == null) {
            throw new ImgSaveCodeNullException();
        } else if(imageInfoDto.getImgName() == null) {
            throw new ImgSaveImgNameNullException();
        }
        int duplicateCheck = imageRepository.DuplicateCheck(imageInfoDto.getVarcd(), imageInfoDto.getVmlcd()); // 중복 확인을 위한
        if(duplicateCheck > 0) { // 차량 제조사와 모델에 해당하는 이미지가 이미 존재한다면
            throw new ImgSaveDuplicateException();
        } else {
            CarImgInfo carImgInfo = QsolModelMapper.map(imageInfoDto, CarImgInfo.class);
            imageRepository.save(carImgInfo);
        }
    }

    public String imgCall(ImageInfoDto imageInfoDto) {
        String imgName = imageRepository.imgCall(imageInfoDto.getVarcd(), imageInfoDto.getVmlcd());
        return imgName;
    }
}
