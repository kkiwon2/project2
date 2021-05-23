package org.kiwon.project.service;

import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.springframework.stereotype.Service;


public interface MemberService {

    public void save(Member member);

}
