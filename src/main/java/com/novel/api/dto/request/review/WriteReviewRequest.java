package com.novel.api.dto.request.review;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriteReviewRequest {

    private int score;
    private String body;

    @Builder
    public WriteReviewRequest(int score, String body) {
        this.score = score;
        this.body = body;
    }
}
