package org.kiwon.project.dto.member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Getter
@Setter
@ToString
public class MemberDTO extends User {

    private String email;
    //password는 부모 클래스를 사용하므로 별도의 멤버 변수로 선언하지 않았음

    private String nickname;

    private boolean fromSocial;

    public MemberDTO(
            String username,    //id
            String password,    //pw
            boolean fromSocial, //소셜여뷰
            Collection<? extends GrantedAuthority> authorities){
    super(username, password, authorities);
    this.email = username;
    this.fromSocial = fromSocial;
    }

}
