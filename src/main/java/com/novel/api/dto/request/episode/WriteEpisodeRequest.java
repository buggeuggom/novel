package com.novel.api.dto.request.episode;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class WriteEpisodeRequest {

    @NotBlank(message = "Write title. Not Blank")
    @Length(min = 2, max = 30, message = "Write title. 2 ~ 30")
    private String title;
    @Length(min = 30, message = "Write content. 30 ~")
    private String content;

    @Builder
    public WriteEpisodeRequest(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
