package com.novel.api.fixture;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.novel.NovelStatus;
import com.novel.api.domain.user.User;

public class NovelFixture {

    public static Novel get(User user){
        return Novel.builder()
                .id(1L)
                .title("test title")
                .explanation("test explanation")
                .genre(Genre.FANTASY)
                .novelStatus(NovelStatus.PROGRESS)
                .user(user)
                .build();
    }
}
