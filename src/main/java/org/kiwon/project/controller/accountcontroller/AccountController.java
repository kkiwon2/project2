package org.kiwon.project.controller.accountcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.entity.member.Member;
import org.kiwon.project.service.board.BoardService;
import org.kiwon.project.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final BoardService boardService;
    private final MemberService memberService;

    @GetMapping("/login")
    public void login(){
        log.info("Account login....");
    }

    @GetMapping("/register")
    public String register(){
        log.info("Account register Get.................");
        return "account/register";
    }

    @PostMapping("/register")
    public String register(Member member){
        log.info("Account register Post.................");
        memberService.save(member);
        return "redirect:/board/list";
    }

    @GetMapping("/duplication")
    public ResponseEntity<Boolean> duplication(String email){
        log.info("Account duplication.................");
        log.info(email);
        Boolean result = memberService.find(email);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/personal")
    public String personal(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("Account personal.................");
        model.addAttribute("memberDTO", memberDTO);
        return "account/personal";
    }

    @PostMapping("/modify")
    public String modify(Member member, @AuthenticationPrincipal MemberDTO memberDTO, Model model){
        log.info("Account Modify.................");
        log.info(member);
        memberService.modify(member);
        memberDTO.setNickname(member.getNickName());
        model.addAttribute("memberDTO", member);
        return "redirect:/board/list";
    }
}
