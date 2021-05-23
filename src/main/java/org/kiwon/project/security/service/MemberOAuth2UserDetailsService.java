package org.kiwon.project.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.member.MemberRole;
import org.kiwon.project.repository.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberOAuth2UserDetailsService extends DefaultOAuth2UserService {
    //OAuth2UserService 인터페이스를 구현한 DefaultOAuth2UserService를 상속한다.
    //@Service가 있으므로 별도의 추가적인 설정이 없어도 자동으로 스프링의 빈으로 등록된다.
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //OAuth2USerRequest객체를 이용하여 현재 어떤 서비스를 통해서 로그인이 이루어졌는지 알아내고, 전달된 값들을 추출할 수 있는 데이터를
    //Map<String, Object>의 형태로 사용할 수 있다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("MemberOAuth2UserDetailsService");
        log.info("userRequest : " + userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clinetName : " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("============================================");
        oAuth2User.getAttributes().forEach((k,v)->{
            log.info(k + ":" + v);  //sub, picture, email, email-verified, EMAL등이 출력됨
        });

        String email = null;

        if(clientName.equals("Google")){
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL : " + email);

        Member member = saveSocialMember(email);

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        memberDTO.setNickname(member.getNickName());

        return memberDTO;
    }
    
    private Member saveSocialMember(String email){
        
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = memberRepository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        //없다면 데이터베이스에 회원 추가 -> 패스워드는 1111 이름은 그냥 이메일 주소로 저장한다.
        Member member = Member.builder()
                .email(email)
                .nickName(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        member.addMemberRole(MemberRole.USER);

        memberRepository.save(member);

        return member;
    }
}
