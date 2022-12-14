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
        // File.seperator ??? OS???????????????.
        // Spring?????? ???????????? cleanPath()??? ????????? ../ ?????? ????????? ????????? ????????? ????????????
        Path copyOfLocation = Paths.get(fileImageDirectory + File.separator + StringUtils.cleanPath(multipartFile.getOriginalFilename()));
        try {
            // inputStream??? ????????????
            // copyOfLocation (????????????)??? ????????? ??????.
            // copy??? ????????? ????????? ???????????? REPLACE(????????????), ??????????????? ??????
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new FileStorageException("Could not store file : " + multipartFile.getOriginalFilename());
        }

    }

    @Transactional // ?????? ????????? ???????????? ?????? ??????
    public void create(ImageInfoDto imageInfoDto) throws NoSuchAlgorithmException {
        if(imageInfoDto.getVarcd() == null || imageInfoDto.getVmlcd() == null) {
            throw new ImgSaveCodeNullException();
        } else if(imageInfoDto.getImgName() == null) {
            throw new ImgSaveImgNameNullException();
        }
        int duplicateCheck = imageRepository.DuplicateCheck(imageInfoDto.getVarcd(), imageInfoDto.getVmlcd()); // ?????? ????????? ??????
        if(duplicateCheck > 0) { // ?????? ???????????? ????????? ???????????? ???????????? ?????? ???????????????
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
