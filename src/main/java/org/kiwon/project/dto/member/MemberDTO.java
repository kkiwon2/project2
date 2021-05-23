package org.kiwon.project.dto.member;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class MemberDTO extends User implements OAuth2User{

    private String email;   //사용자 아이디

    private String password;    //사용자 비번

    private String nickname;    //사용자 닉네임

    private boolean fromSocial; //소셜사용 여부

    private Map<String, Object> attr;   //소셜로그인을 한 데이터는 OAuth2User의 Map<String, Object>형태로 반환함

    public MemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities,
            Map<String, Object> attr){
        
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }


    public MemberDTO(
            String username,
            String password,
            boolean fromSocial,
            Collection<? extends GrantedAuthority> authorities ){
        
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public String getName() {
        return null;
    }
}
