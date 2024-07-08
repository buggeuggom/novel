package com.novel.api.dto.request.episode;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriteEpisodeRequest {

    private String title;
    private String content;

    @Builder
    public WriteEpisodeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
