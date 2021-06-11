package org.kiwon.project.repository;

import org.junit.jupiter.api.Test;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.entity.member.MemberRole;
import org.kiwon.project.repository.member.MemberRepository;
import org.kiwon.project.repository.movie.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            Member member = Member.builder()
                    .email("r" + i + "@zerock.org")
                    .password(passwordEncoder.encode("1111"))
                    .nickName("reviewer" + i)
                    .fromSocial(false)
                    .build();
            member.addMemberRole(MemberRole.USER);
            memberRepository.save(member);
        });
    }

    //특정 회원을 삭제시 리뷰테이블을 먼저 삭제하고 회원테이블의 특저 회원 삭제하기 -> 2개의 작업을 트랜잭션으로 묶어놔야됨
    @Commit
    @Transactional
    @Test
    public void testDeleteMember(){
        String email = "r99@zerock.org";

        Member member = Member.builder().email(email).build();

        //순서 주의 -> FK를 가진 쪽을 먼저 처리해야됨
        reviewRepository.deleteByMember(member);
        memberRepository.deleteById(email);
    }
}
