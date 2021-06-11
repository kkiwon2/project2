package org.kiwon.project.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page;       //화면에 보이는 페이지 사이즈
    private int size;       //화면에 보이는 페이지 개수
    private String type;    //검색 타입
    private String keyword; //검색 키워드

    public PageRequestDTO(){    //기본 값은 1페이지 10개씩
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page -1, size, sort);
    }

}
