package com.novel.api.dto.response.episode;

import com.novel.api.dto.EpisodeDto;
import lombok.Getter;

@Getter
public class EpisodeResponse {

    private String title;
    private String content;

    private EpisodeResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static EpisodeResponse from(EpisodeDto dto) {
        return new EpisodeResponse(
                dto.getTitle(),
                dto.getDetail());
    }
}
