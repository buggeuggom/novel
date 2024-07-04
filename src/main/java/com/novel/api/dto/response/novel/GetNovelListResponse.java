package com.novel.api.dto.response.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.NovelStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetNovelListResponse {

    private String title;
    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;
    private String author;

    @Builder
    private GetNovelListResponse(String title, String explanation, NovelStatus novelStatus, Genre genre, String author) {
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
        this.author = author;
    }
}
