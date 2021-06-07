package org.kiwon.project.controller.accountcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.service.BoardService;
import org.kiwon.project.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("/login")
    public void login(){
        log.info("account login....");
    }

    @GetMapping("/register")
    public String register(){

        return "account/register";
    }

    @PostMapping("/register")
    public String register(Member member){
        memberService.save(member);
        return "redirect:/board/list";
    }

    @GetMapping("/duplication")
    public ResponseEntity<Boolean> duplication(String email){
        log.info("duplication 메소드 실행");
        log.info(email);
        Boolean result = memberService.find(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
