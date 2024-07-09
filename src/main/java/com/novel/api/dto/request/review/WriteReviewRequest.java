package com.novel.api.dto.request.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriteReviewRequest {

    private int score;
    private String content;

    @Builder
    public WriteReviewRequest(int score, String content) {
        this.score = score;
        this.content = content;
    }
}
