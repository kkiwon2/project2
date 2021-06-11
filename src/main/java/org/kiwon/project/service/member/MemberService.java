package org.kiwon.project.service.member;

import org.kiwon.project.dto.board.BoardDTO;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.board.Board;
import org.kiwon.project.entity.member.Member;
import org.springframework.stereotype.Service;


public interface MemberService {

    //회원정보 등록
    public void save(Member member);

    //
    public boolean find(String email);

    //회원정보 수정
    public void modify(Member member);

//    default Member dtoToEntity(MemberDTO dto) {
//
//        Member member = Member.builder()
//                .email(dto.getEmail())
//                .password(dto.getPassword())
//                .nickName(dto.getNickname())
//                .fromSocial(false)
//                .build();
//
//        return member;
//    }

//    default MemberDTO entityToDTO(Member member) {
//
//        MemberDTO memberDTO = MemberDTO.builder()
//                .email(member.getEmail())
//                .password(member.getPassword())
//                .nickname(member.getNickName())
//                .fromSocial(member.isFromSocial())
//                .build();
//        return memberDTO;
//    }

}

