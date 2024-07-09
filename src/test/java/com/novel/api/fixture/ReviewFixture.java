package com.novel.api.fixture;

import com.novel.api.domain.review.Review;

public class ReviewFixture {
    public static Review get(){
        return Review.builder()
                .id(301L)
                .user(UserFixture.get())
                .novel(NovelFixture.get())
                .score(3)
                .body("test review body")
                .build();
    }
}
