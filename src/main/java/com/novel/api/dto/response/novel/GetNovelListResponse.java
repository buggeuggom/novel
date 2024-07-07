package com.novel.api.dto.response.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.novel.NovelStatus;
import lombok.Getter;

@Getter
public class GetNovelListResponse {

    private String title;
    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;
    private String author;

    /*
    페이징에서 사용
     */
    public GetNovelListResponse(Novel entity) {
        this.title = entity.getTitle();
        this.explanation = entity.getExplanation();
        this.novelStatus = entity.getNovelStatus();
        this.genre = entity.getGenre();
        this.author = entity.getUser().getName();
    }
}
