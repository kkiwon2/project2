package org.kiwon.project.service;

import lombok.RequiredArgsConstructor;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.member.MemberRole;
import org.kiwon.project.repository.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
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

        if (id.isPresent())
        {
            return true;
        }

        return false;
    }
}
