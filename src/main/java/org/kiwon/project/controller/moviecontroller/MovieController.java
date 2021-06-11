package org.kiwon.project.controller.moviecontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.kiwon.project.dto.member.MemberDTO;
import org.kiwon.project.dto.movie.MovieDTO;
import org.kiwon.project.dto.page.PageRequestDTO;
import org.kiwon.project.dto.page.PageResultDTO;
import org.kiwon.project.entity.movie.Movie;
import org.kiwon.project.service.movie.MovieService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/movie")
@Log4j2
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    //영화 등록 메서드 -> /movie/register.html 호출
    @GetMapping("/register")
    public void register(@AuthenticationPrincipal MemberDTO memberDTO, Model model){
        model.addAttribute("memberDTO", memberDTO);
        log.info("MovieController Get register..............");
    }

    // 영화 등록을 위한 파라미터로 전달된 데이터를 MovieDTO로 수집해서 처리
    @PostMapping("/register")
    public String register(MovieDTO movieDTO, RedirectAttributes redirectAttributes){
        log.info("MovieController Post register..............");
        log.info("movieDTO : " + movieDTO);
        
        Long mno = movieService.register(movieDTO);

        redirectAttributes.addFlashAttribute("msg", mno);

        //영화가 등록된 후에는 목록 페이지로 이동
        return "redirect:/movie/list";
    }

    //영화 목록 처리
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("MovieController list...............");
        log.info("pageRequestDTO : " + pageRequestDTO);

        model.addAttribute("result", movieService.getList(pageRequestDTO));
    }

    //영화 조회 페이지 - 수정 작업에서도 동일한 기능을 요구함으로 배열로 묶음
    @GetMapping({"/read", "/modify"})
    public void read(long mno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model,
                     @AuthenticationPrincipal MemberDTO memberDTO){
        log.info("MovieController read...............");
        log.info("mno : " + mno);

        MovieDTO movieDTO = movieService.getMovie(mno);

        model.addAttribute("dto", movieDTO);
        model.addAttribute("memberDTO", memberDTO);
    }
}
