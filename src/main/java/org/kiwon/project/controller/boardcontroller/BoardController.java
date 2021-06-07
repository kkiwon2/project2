package org.kiwon.project.controller.boardcontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.board.BoardDTO;
import org.kiwon.project.dto.board.PageRequestDTO;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.service.BoardService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO,  Model model){

        log.info("list........");
        log.info("PageRequestDTO : " + pageRequestDTO);
        model.addAttribute("result", boardService.getList(pageRequestDTO));

    }

    @GetMapping("/register")
    public void register(@AuthenticationPrincipal MemberDTO memberDTO, BoardDTO boardDTO){

        boardDTO.setWriterEmail(memberDTO.getEmail());
        boardDTO.setWriterName(memberDTO.getNickname());
        log.info("register get..........");

    }

    @PostMapping("/register")
    public String registerPost(BoardDTO boardDTO, RedirectAttributes redirectAttributes){

        log.info("register post......");
        log.info("dto : " + boardDTO);

        Long bno = boardService.register(boardDTO);

        log.info("register bno : " + bno);

        redirectAttributes.addFlashAttribute("msg",bno);
        return "redirect:/board/list";
    }

    //게시판 상세 조회 요청
    @GetMapping({"/read", "/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model,
                     @AuthenticationPrincipal MemberDTO memberDTO){

        log.info("read get........");
        log.info("bno : " + bno);

        BoardDTO boardDTO = boardService.get(bno);

        log.info("boardDTO : " + boardDTO);

        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("dto", boardDTO);
    }

    @PostMapping("/modify")
    public String modify(BoardDTO dto, @ModelAttribute("requestDTO")PageRequestDTO pageRequestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("post modify.................................");
        log.info("dto : " + dto);

        boardService.modify(dto);

        redirectAttributes.addAttribute("page", pageRequestDTO.getPage());
        redirectAttributes.addAttribute("type", pageRequestDTO.getType());
        redirectAttributes.addAttribute("keyword", pageRequestDTO.getKeyword());
        redirectAttributes.addAttribute("bno", dto.getBno());

        return "redirect:/board/read";
    }

    @PostMapping("/remove")
    public String remove(Long bno, RedirectAttributes redirectAttributes){

        log.info("remove post.......");

        boardService.removeWithReplies(bno);

        redirectAttributes.addFlashAttribute("msg",bno);

        return  "redirect:/board/list";
    }


}