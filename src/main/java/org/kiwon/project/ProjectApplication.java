package org.kiwon.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
//스프링 부트의 자동 설정, 스프링 Bean 읽기와 생성을 모두 자동으로 설정됩니다.
//또한, @SpringBootApplication이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야 합니다.
@SpringBootApplication
public class ProjectApplication {

    public static void main(String[] args) {
        //run()으로 인해 내장 WAS를 실행합니다. -> 이렇게 되면 항상 서버에 톰캣을 설치할 필요가 없게 되고, 스프링부트로 만들어진 Jar파일(실행 가능한 Java 패키징 파일)로 실행하면 됩니다
        SpringApplication.run(ProjectApplication.class, args);
    }

}
