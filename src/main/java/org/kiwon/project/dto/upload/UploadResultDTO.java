package org.kiwon.project.dto.upload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@AllArgsConstructor
public class UploadResultDTO implements Serializable {
    // 업로드된 파일의 원래이름, 파일의 UUID값, 업로드된 파일의 저장경로가 브라우저에서 필요하다
    // 브라우저에서 처리가 간단할 수 있도록 클래스를 구성했다.

    private String fileName;
    private String uuid;
    private String folderPath;

    //나중에 전체 경로가 필요한 경우를 대비한 메서드임
    public String getImageURL(){
        try{
            return URLEncoder.encode(folderPath + "/" + uuid + "_" + fileName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    //브라우저에서 섬네일링크 처리를 위한 메서드
    public String getThumbnailURL(){
        try{
            return URLEncoder.encode(folderPath + "/s_" + uuid + "_" + fileName, "UTF-8");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }


}
