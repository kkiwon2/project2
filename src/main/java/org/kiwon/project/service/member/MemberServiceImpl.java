package org.kiwon.project.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.member.MemberRole;
import org.kiwon.project.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    //사용자의 패스워드를 인코딩
    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(Member member) {
        String encodePassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encodePassword);
        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);
    }

    @Override
    public boolean find(String email) {
        Optional<String> id = memberRepository.findByEmail2(email);

        if (id.isPresent()) {
            return true;
        }

        return false;
    }

    @Override
    public void modify(Member member) {

        Member member2 = memberRepository.findById(member.getEmail()).get();

        //이메일로 찾은 결과가 null이 아니라면
        if (member2 != null) {

            log.info(member2);
            String encodePassword = passwordEncoder.encode(member.getPassword());
            member2.setPassword(encodePassword);
            member2.setNickName(member.getNickName());
            member2.addMemberRole(MemberRole.USER);
            memberRepository.save(member2);

//            String encodePassword = passwordEncoder.encode(memberDTO.getPassword());
//
//            member.changePassword(encodePassword);
//            member.changeNickName(memberDTO.getNickname());
//            member.addMemberRole(MemberRole.USER);
//            memberRepository.save(member);

        }
    }
}
