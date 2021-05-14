package org.kiwon.project.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //패스워드를 인코딩하는 것이 주 목적인 PasswordEncoder를 Bean으로 등록
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //Bcrypt라는 해시 함수를 이용해서 암호화하는 목적으로 설계된 클래스를 사용
    }

//    //코드를 통해서 직접 인증 매니저를 설정 할 때 사용하는 메서드
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("kkiwon2")
//                .password("$2a$10$UXeN.UtmR62WoVhSfkJke.x37s1MS/ZRsB1P0ZUw/lyu4RDXYQzQ2")
//                .roles("USER");
//    }

    //인가(Authorization)가 필요한 리소스 설정을 위한 메서드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //설정을 통한 접근제한을 처리
        http.authorizeRequests()
                .antMatchers("/board/list").hasRole("USER");

        //인가 OR 인증에 문제시 로그인 화면을 나타냄
        http.formLogin();
        http.logout();  //CSRF를 사용할때는 POST방식으로만 로그아웃을 처리한다.
        //CSRF 토근 발행 비활성화
        http.csrf().disable();
    }
}
