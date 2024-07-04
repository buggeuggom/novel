package com.novel.api.dto.request.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.NovelStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PutNovelRequest {

    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;

    @Builder
    public PutNovelRequest(String explanation, NovelStatus novelStatus, Genre genre) {
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
    }
}
