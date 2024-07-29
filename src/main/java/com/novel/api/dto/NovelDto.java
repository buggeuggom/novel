package com.novel.api.dto;

import com.novel.api.domain.novel.Genre;
import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.novel.NovelStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NovelDto {

    private Long id;

    private String title;
    private String explanation;
    private NovelStatus novelStatus;
    private Genre genre;
    private UserDto userDto;
    private LocalDateTime createdAt;


    @Builder
    private NovelDto(Long id, String title, String explanation, NovelStatus novelStatus, Genre genre, UserDto userDto, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.explanation = explanation;
        this.novelStatus = novelStatus;
        this.genre = genre;
        this.userDto = userDto;
        this.createdAt = createdAt;
    }

    public static NovelDto from(Novel entity) {
        return NovelDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .explanation(entity.getExplanation())
                .novelStatus(entity.getNovelStatus())
                .genre(entity.getGenre())
                .userDto(UserDto.from(entity.getUser()))
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
