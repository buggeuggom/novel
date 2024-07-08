package com.novel.api.fixture;

import com.novel.api.domain.episode.Episode;

public class EpisodeFixture {

    public static Episode get() {
        return Episode.builder()
                .id(201L)
                .title("test episode title")
                .detail("test episode detail")
                .novel(NovelFixture.get(UserFixture.get()))
                .build();
    }
}
