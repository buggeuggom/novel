package com.novel.api.dto;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.novel.NovelStatus;
import com.novel.api.domain.user.User;
import com.novel.api.domain.user.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

@Getter
public class NovelDto {

    private Long id;

    private String title;
    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;
    private UserDto userDto;

    @Builder
    private NovelDto(Long id, String title, String explanation, NovelStatus novelStatus, Genre genre, UserDto userDto) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
        this.userDto = userDto;
    }

    public static NovelDto from(Novel entity) {
        return NovelDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .explanation(entity.getExplanation())
                .novelStatus(entity.getNovelStatus())
                .genre(entity.getGenre())
                .userDto(UserDto.fromEntity(entity.getUser()))
                .build();
    }
}
