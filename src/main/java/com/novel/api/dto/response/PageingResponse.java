package com.novel.api.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageingResponse<T> {

    private final long page;
    private final long size;
    private final long totalCount;
    private final List<T> items;


    public PageingResponse(Page<?> page, Class<T> clazz) {
        this.page = page.getNumber() + 1;
        this.size = page.getSize();
        this.totalCount = page.getTotalElements();
        this.items = page.getContent().stream().map(cont -> {
                    try {
                        return clazz.getConstructor(cont.getClass()).newInstance(cont);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
