package com.novel.api.dto.request.novel;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static java.lang.Math.min;

@Getter
@Setter
@Builder
public class GetSubscribeNovelListSearch {

    private static final int MAX_PAGE = 999;
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;
    @Builder.Default
    private Integer size = 10;

    public void setPage(Integer page) {
        this.page = page <= 0 ? 1 : min(page, MAX_PAGE);
    }

    public long getOffset() {
        return (long) (page - 1) * min(size, MAX_SIZE);
    }

    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }


}
