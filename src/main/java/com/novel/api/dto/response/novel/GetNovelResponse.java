package com.novel.api.dto.response.novel;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.NovelStatus;
import com.novel.api.dto.NovelDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class GetNovelResponse {

    private String title;
    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;
    private String authorName;

    private GetNovelResponse(String title, String explanation, NovelStatus novelStatus, Genre genre, String authorName) {
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
        this.authorName = authorName;
    }

    public static GetNovelResponse from(NovelDto dto){
        return new GetNovelResponse(
                dto.getTitle(),
                dto.getExplanation(),
                dto.getNovelStatus(),
                dto.getGenre(),
                dto.getUserDto().getName()
        );
    }
}
