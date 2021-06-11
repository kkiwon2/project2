package org.kiwon.project.controller.fileuploadconroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/uploadTest")
public class UploadTestController {
    //테스트를 위한 컨트롤 화면이며, 실제 업로드는 브라우저상에서 jQuery로 처리할 것이므로 Get 방식으로 화면을 볼 수 있도록 구성합니다.
    @GetMapping("/uploadEx")
    public void uploadEx(){

    }
}
