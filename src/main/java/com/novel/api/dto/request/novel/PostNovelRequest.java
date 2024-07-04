package com.novel.api.dto.request.novel;

import com.novel.api.domain.novel.Genre;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostNovelRequest {


    private String title;
    private String explanation;
    private Genre genre;

    @Builder
    public PostNovelRequest(String title, String explanation, Genre genre) {
        this.title = title;
        this.explanation = explanation;
        this.genre = genre;
    }
}
