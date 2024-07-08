package com.novel.api.dto;

import com.novel.api.domain.episode.Episode;
import com.novel.api.domain.novel.Novel;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class EpisodeDto {

    private Long id;

    private String title;
    private String detail;
    private LocalDateTime createdAt;

    private NovelDto novelDto;


    public EpisodeDto(Long id, String title, String detail, LocalDateTime createdAt, NovelDto novelDto) {
        this.id = id;
        this.title = title;
        this.detail = detail;
        this.createdAt = createdAt;
        this.novelDto = novelDto;
    }

    public static EpisodeDto from(Episode entity) {
        return new EpisodeDto(
                entity.getId(),
                entity.getTitle(),
                entity.getDetail(),
                entity.getCreatedAt(),
                NovelDto.from(entity.getNovel()));
    }

}
