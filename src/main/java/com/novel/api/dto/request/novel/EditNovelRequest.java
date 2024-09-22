package com.novel.api.dto.request.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.NovelStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class EditNovelRequest {

    @Length(min = 10, message = "Write explanation. 10 ~")
    @NotBlank(message = "Write explanation. Not Blank")
    private String explanation;
    @NotBlank(message = "Write novelStatus. Not Blank")
    private NovelStatus novelStatus;
    private Genre genre;

    @Builder
    public EditNovelRequest(String explanation, NovelStatus novelStatus, Genre genre) {
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
    }
}
