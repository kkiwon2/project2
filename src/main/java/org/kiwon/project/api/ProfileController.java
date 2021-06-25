package org.kiwon.project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final Environment env;  //스프링부트의 환경변수? -> 인터페이스임

    @GetMapping("/profile")
    public String profile() {
        //현재 실행 중인 ActiveProfile을 모두 가져옵니다.
        //즉 real, oauth, real-db 등이 활성화되어 있다면(active) 3개가 모두 담겨있습니다. -> 나는 아님
        List<String> profiles = Arrays.asList(env.getActiveProfiles());
        List<String> realProfiles = Arrays.asList("real", "real1", "real2");
        String defaultProfile = profiles.isEmpty() ? "default" : profiles.get(0);

        return profiles.stream().filter(realProfiles::contains)
                .findAny()
                .orElse(defaultProfile);
    }

}
