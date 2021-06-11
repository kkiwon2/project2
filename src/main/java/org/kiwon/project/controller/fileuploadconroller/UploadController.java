package org.kiwon.project.controller.fileuploadconroller;

import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.kiwon.project.dto.upload.UploadResultDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@Log4j2
public class UploadController {

    //application파일에 설정한 파일을 저장할 경로
    @Value("${org.zerock.upload.path}") //application.properties의 변수 -> 설정한 경로는 C:\\upload
    private String uploadPath;

    @PostMapping("/uploadAjax")
    //실제 업로드된 파일 처리는 컨트롤러로 처리하며, 이와 관련해서 스프링에서는 MultipartFile 타입을 제공한다.
    //UploadResultDTO는 실제 파일과 관련된 모든 정보를 가지고있다. -> 업로드 결과를 반환하기 위해서 ResponseEntity를 이용해서 처리한다
    public ResponseEntity<List<UploadResultDTO>> uploadFile(MultipartFile[] uploadFiles){

        List<UploadResultDTO> resultDTOList = new ArrayList<>();

        log.info("UploadController uploadAjax...........");
        for(MultipartFile uploadFile : uploadFiles){

            //파일을 저장하는 단계에서는 업로드된 확장자가 이미지만 가능하도록 검사, 동일한 이름의 파일이 업로드 된다면 기존 파일을 덮어쓰는 문제를 해결해야됨
            //파일 이름은 UUID값_파일명 형태로 저장합니다.
            //첨부파일을 이용해서 쉘 스크립트 공격하는 기법을 막기 위해 브라우저에서 파일을 업로드하는 수간이나, 서버에서 파일을 저장하는 수간에 검사해야됨
            //MultipartFile에서 제공하는 getContentType()를 이용해서 막을 수 있음

            //이미지 파일만 업로드 가능하게 getContentType()를 이용해서 검사한다.
            if(uploadFile.getContentType().startsWith("image") == false){
                log.warn("이 파일은 이미지 파일이 아닙니다.");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);  //403에러 발생
            }

            //실제 파일 이름은 IE나 Edge는 전체 경로가 들어오며, 크롬은 단순히 파일 이름만 들어옴 -> 둘 다 파일 이름만 들어오는데?....
            String originalName = uploadFile.getOriginalFilename();
            log.info("originalName : " + originalName);
            String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);
            log.info("fileName : " + fileName);

            //동일한 폴더에 너무 많은 파일이 저장되면 성능저하 발생 -> 년/월/일 폴더를 생성
            String folderPath = makeFolder();
            log.info("folderPath : " + folderPath);

            //UUID
            String uuid = UUID.randomUUID().toString();

            //저장할 파일 이름 중간에 "_"를 이용해서 구분 -> UUID_파일이름
            // C:\\upload + 년/월/일 + uuid + _ + fileName
            String saveName = uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
            log.info("saveName : " + saveName);

            Path savePath = Paths.get(saveName);
            log.info("savePath : " +savePath);

            try{
                //실제로 업로드된 파일을 저장해야됨 -> MultipartFile 자체에서 제공하는 transferTo()를 이용하면 간단히 파일을 저장 가능
                uploadFile.transferTo(savePath);    //원본 파일 저장

                //섬네일 파일 이름은 중간에 s_로 시작하도록 설정
                String thumbnailSaveName = uploadPath + File.separator + folderPath + File.separator
                        + "s_" + uuid + "_" + fileName;
                log.info("thumbnailSaveName : " + thumbnailSaveName);

                //섬네일 생성
                File thumbnailFile = new File(thumbnailSaveName);
                Thumbnailator.createThumbnail(savePath.toFile(), thumbnailFile, 100, 100);

                resultDTOList.add(new UploadResultDTO(fileName, uuid, folderPath));
            }catch (IOException e){
                e.printStackTrace();
            }
        }//end for

        return new ResponseEntity<>(resultDTOList, HttpStatus.OK);
    }//end PostMapping -> /uploadAjax

    private String makeFolder(){
        log.info("makFolder()..................");

        String str = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        log.info("str : " + str);

        String folderPath = str.replace("//", File.separator);
        log.info("folderPath : " + folderPath);

        //make folder --------------------
        File uploadPathFolder = new File(uploadPath, folderPath);
        log.info("uploadPathFolder : " + uploadPathFolder);

        if(uploadPathFolder.exists() == false){
            log.info("폴더가 없으므로 폴더 생성");
            uploadPathFolder.mkdirs();
        }

        log.info("makeFolder() 종료...................");
        return folderPath;

    }

    //URL 인코딩된 파일 이름을 파라미터로 받아서 해당 파일을 byte[]로 만들어서 브라우저로 전송하는 메서드입니다.
    //ream.html 파일로부터 fileName과, size라는 파라미터를 받아서 원본 파일인지 섬네일인지 구분할 수 있도록 구성했습니다.
    //만일 size 변수의 값이 1인 경우 원본 파일을 전송합니다.
    @GetMapping("/display")
    public ResponseEntity<byte[]> getFile(String fileName, String size){

        log.info("UploadController getFile...........");
        ResponseEntity<byte[]> result = null;

        log.info("fileName 디코딩 전 : " + fileName);
        try {
            String srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("srcFileName 디코딩 후 : " + srcFileName);

            File file = new File(uploadPath + File.separator + srcFileName);
            log.info("file : " + file);

            //size라는 파라미터를 이용해서 원본 파일인지 섬네일인지 구분하는 조건문
            //1이라면 원본 파일을 전송합니다.
            if(size != null && size.equals("1")){
                file = new File(file.getParent(), file.getName().substring(2));
                log.info("size파라미터가 1일 때 file : " + file);
            }

            HttpHeaders header = new HttpHeaders();

            //MIME타입 처리 -> 파일의 확장자에 따라서 브라우저에 전송하는 MIME타입이 달라져야 하는 문제 -> Files.probeContentType()로 해결
            header.add("Content-Type", Files.probeContentType(file.toPath()));

            //파일 데이터 처리, 실제 업로드된 파일을 저장할 때 스프링 자체에서 제공하는 FileCopyUtils
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }

    //업로드된 파일 삭제 -> 원본 파일의 이름을 파라미터로 전송받은 후에 File 객체를 이용해서 원본과 섬네이을 같이 삭제하는 메서드
    @PostMapping("/removeFile")
    public ResponseEntity<Boolean> removeFile(String fileName){
        log.info("UploadController removeFile...........");

        String srcFileName = null;
        try{
            //원본 파일 삭제
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            log.info("srcFileName 디코딩 후 : " + srcFileName);
            File file = new File(uploadPath + File.separator + srcFileName);
            boolean result = file.delete();

            //섬네일 파일 삭제
            File thumbnail = new File(file.getParent(), "s_" + file.getName());
            log.info("thumbnail : " + thumbnail);
            result = thumbnail.delete();

            log.info("removeFile 종료...........");
            return new ResponseEntity<>(result, HttpStatus.OK);
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }//end removeFile()
}
