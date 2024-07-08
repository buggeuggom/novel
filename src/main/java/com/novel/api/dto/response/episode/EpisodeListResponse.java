package com.novel.api.dto.response.episode;

import com.novel.api.domain.episode.Episode;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EpisodeListResponse {

    private String title;
    private LocalDateTime createdAt;

    public EpisodeListResponse(Episode entity) {
        this.title = entity.getTitle();
        this.createdAt = entity.getCreatedAt();
    }
}
