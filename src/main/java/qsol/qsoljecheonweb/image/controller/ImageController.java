package qsol.qsoljecheonweb.image.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import qsol.qsoljecheonweb.image.model.response.ImageInfoDto;
import qsol.qsoljecheonweb.image.service.ImageService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;
    @Value("${file.image.directory}")
    private String fileImageDirectory;

    @PostMapping("/imgSave")
    public String fileUpload(MultipartFile file, RedirectAttributes redirectAttributes) {
        if(file != null) {
            imageService.fileUpload(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
            return "redirect:/";
        } else {
            return null;
        }
    }

    @PostMapping("/imgInfoSave") // 차량 이미지 테이블에 정보 저장
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid ImageInfoDto imageInfoDto) throws NoSuchAlgorithmException {
        log.info(" --- imgInfoSave create called --- {}", imageInfoDto);
        imageService.create(imageInfoDto);
        return ResponseEntity.ok().build();
    }

    /*@PostMapping("/imgCall")
    public String imgCall(@RequestBody @Valid ImageInfoDto imageInfoDto) {
        String imgName = imageService.imgCall(imageInfoDto);
        return imgName;
    }*/
    @PostMapping("/imgCall")
    public String imgCall(@RequestBody @Valid ImageInfoDto imageInfoDto) {
        String imgName = imageService.imgCall(imageInfoDto);
        if( imgName == null || imgName.equals(""))  //등록된 이미지가 없는 경우 기본이미지로 대체하기
            imgName = "Default.png";
        return imgName;
    }

    //이미지 경로 및 파일명을 사용하여 이미지 보여주기
    @GetMapping("/LoadImg")
    public void GetImageForFileName(HttpServletRequest request, HttpServletResponse response  )  throws Exception  {
        String imgName = request.getParameter("img"); //이미지 파일명
        String ImgDir = fileImageDirectory; //이미지 저장 폴더 경로
        imgName = ImgDir +  File.separator + imgName; //전체 이미지 경로 및 파일명
        ImageView( response, imgName);  //이미지 파일 보여주기
    }

    //운영서버의 이미지를 보여주는 기능
    public void ImageView(HttpServletResponse response, String imgpath )  throws Exception {
        ServletOutputStream Imgout = response.getOutputStream(); //응답 서블릿

        File ImgFile = new File(imgpath); //불러오는 이미지 파일경로 및 파일명
        if (ImgFile.exists()) { //파일이 있는 경우 Stream으로 이미지 파일 읽어서 웹에 보여주기
            FileInputStream f = new FileInputStream(imgpath);
            int length;
            byte[] buffer = new byte[10];
            while ((length = f.read(buffer)) != -1)
                Imgout.write(buffer, 0, length); //응답 서블릿에 이미지 처리
        }
    }


    @PostMapping("/imgDelete")
    public ResponseEntity<HttpStatus> imgDelete(@RequestBody @Valid ImageInfoDto imageInfoDto) {
        log.info(" --- imgDelete called --- {}", imageInfoDto);
        imageService.imgDelete(imageInfoDto);
        return ResponseEntity.ok().build();
    }
}
