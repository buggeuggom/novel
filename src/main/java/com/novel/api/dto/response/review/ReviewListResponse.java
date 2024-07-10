package com.novel.api.dto.response.review;


import com.novel.api.domain.review.Review;

public class ReviewListResponse {

    private Long id;
    private String userName;
    private Integer score;
    private String body;

    public ReviewListResponse(Review entity) {
        this.id = entity.getId();
        this.userName = entity.getUser().getName();
        this.score = entity.getScore();
        this.body = entity.getBody();
    }
}
