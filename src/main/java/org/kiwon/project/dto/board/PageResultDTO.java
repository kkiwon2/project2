package org.kiwon.project.dto.board;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Log4j2
public class PageResultDTO<DTO, EN> {

    private List<DTO> dtoList;

    private int totalPage;

    private int page;
    private int size;

    private int start, end;

    private boolean prev, next;

    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result , Function<EN, DTO> fn){
        super();
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        totalPage = result.getTotalPages();

        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        log.info("PageResultDTO..........................");
        this.page = pageable.getPageNumber() + 1;    //페이지 번호는 0부터 시작하므로
        this.size = pageable.getPageSize();

        System.out.println("page" + page);
        System.out.println("size" + size);

        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;

        start = tempEnd - 9;

        prev = start > 1;

        end = totalPage > tempEnd ? tempEnd : totalPage;

        next = totalPage > tempEnd;

        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }

}
