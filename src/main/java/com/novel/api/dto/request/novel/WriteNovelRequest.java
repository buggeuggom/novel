package com.novel.api.dto.request.novel;

import com.novel.api.domain.novel.Genre;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class WriteNovelRequest {

    @NotBlank(message = "Write title. Not Blank")
    private String title;
    @Length(min = 10, message = "Write explanation. 10 ~")
    @NotBlank(message = "Write explanation. Not Blank")
    private String explanation;
    private Genre genre;

    @Builder
    public WriteNovelRequest(String title, String explanation, Genre genre) {
        this.title = title;
        this.explanation = explanation;
        this.genre = genre;
    }
}
