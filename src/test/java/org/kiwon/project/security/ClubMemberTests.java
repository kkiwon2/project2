package org.kiwon.project.security;

import org.junit.jupiter.api.Test;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.member.MemberRole;
import org.kiwon.project.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ClubMemberTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies(){

        IntStream.rangeClosed(1,1).forEach(i -> {
            Member member = Member.builder()
                    .email("kkiwon2")
                    .nickName("기원")
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();

            member.addMemberRole(MemberRole.USER);

            memberRepository.save(member);
        });
    }

//    @Test
//    public void testRead() {
//        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@zerock.org", false);
//
//        ClubMember clubMember = result.get();
//
//        System.out.println(clubMember);
//    }
}
