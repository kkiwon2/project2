package org.kiwon.project.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.repository.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService {
    //이 클래스가 스프링의 Bean으로 등록되면 이를 자동으로 스프링 시큐리티에서 UserDetailsService로 인식한다.

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("MemberUserDetailsService");
        log.info("loadUserByUsername : " + username );

        Optional<Member> result = memberRepository.findByEmail(username, false);

        if(!result.isPresent()){
            throw  new UsernameNotFoundException("Check Email or Social");
        }

        Member member = result.get();

        log.info("Member : " + member);

        MemberDTO memberDTO = new MemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_"+role.name())).collect(Collectors.toSet()));

        memberDTO.setNickname(member.getNickName());
        memberDTO.setFromSocial(member.isFromSocial());

        return memberDTO;
    }
}
