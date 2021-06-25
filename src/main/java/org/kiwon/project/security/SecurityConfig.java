package org.kiwon.project.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.security.service.MemberUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Log4j2
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final MemberUserDetailsService memberUserDetailsService;

    //패스워드를 인코딩하는 것이 주 목적인 PasswordEncoder를 Bean으로 등록
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); //Bcrypt라는 해시 함수를 이용해서 암호화하는 목적으로 설계된 클래스를 사용
    }



    //인가(Authorization)가 필요한 리소스 설정을 위한 메서드
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //설정을 통한 접근제한을 처리
        //authorizeRequests()가 선언되어야만 antMatchers 옵션을 사용할 수 있습니다.
        //antMatchers()는 권한 관리 대상을 지정하는 옵션입니다.
        //anyRequest()는 설정된 값을 이외 나머지 URL들을 나타냅니다.
        //authenticated()를 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용합니다. 인증된 사용자는, 로그인한 사용자들을 말함
        http.authorizeRequests()
                .antMatchers("/","/account/**","/css/**","/vendor/**","/img/**","/uploadTest/**").permitAll()
                .antMatchers("/board/list","/board/**","/replies/**","/account/personal").hasRole("USER")
                .anyRequest().authenticated()
                .and()
        //인가 OR 인증에 문제시 로그인 화면을 나타냄
        .formLogin().loginPage("/account/login").permitAll();

        //로그아웃 기능에 대한 여러 설정의 진입점입니다.
        //로그아웃 성공시 "/"주소로 이동합니다.
        http.logout().logoutSuccessUrl("/"); //CSRF를 사용할때는 POST방식으로만 로그아웃을 처리한다.

        // CSRF 토근 발행 비활성화
        http.csrf().disable();

        //rememberMe()설정
        http.rememberMe().tokenValiditySeconds(60*60*7).userDetailsService(memberUserDetailsService);

        //OAuth2 로그인 기능에 대한 여러 설정의 진입점입니다.
        http.oauth2Login();

        //사용자가 적절한 권한이 없어서 해당 페이지를 볼 수 없을때 보여줄 화면
        http.exceptionHandling().accessDeniedPage("/error");
    }







    //    //코드를 통해서 직접 인증 매니저를 설정 할 때 사용하는 메서드
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("kkiwon2")
//                .password("$2a$10$UXeN.UtmR62WoVhSfkJke.x37s1MS/ZRsB1P0ZUw/lyu4RDXYQzQ2")
//                .roles("USER");
//    }
}
