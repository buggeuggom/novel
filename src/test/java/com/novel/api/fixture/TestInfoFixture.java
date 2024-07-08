package com.novel.api.fixture;

import com.novel.api.domain.novel.Genre;
import lombok.Data;

import static com.novel.api.domain.novel.NovelStatus.PROGRESS;

public class TestInfoFixture {
    public static TestInfo  get(){
        TestInfo info = new TestInfo();

        info.setUserId(101L);
        info.setUserName("test user");
        info.setPassword("ads1231");

        info.setNovelId(1L);
        info.setTitle("test title");
        info.setExplanation("test explanation");
        info.setGenre(Genre.FANTASY);

        info.setEpisodeId(201L);
        info.setEpisodeTitle("test episode title");
        info.setEpisodeDetail("test episode detail");
        return info;
    }


    @Data
    public static class TestInfo{
        private Long novelId;
        private Long userId;
        private String userName;
        private String password;
        private String title;
        private String explanation;
        private Genre genre;

        private Long episodeId;
        private String episodeTitle;
        private String episodeDetail;
    }
}
