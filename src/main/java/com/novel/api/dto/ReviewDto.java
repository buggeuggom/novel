package com.novel.api.dto;

import com.novel.api.domain.review.Review;
import lombok.Getter;

@Getter
public class ReviewDto {

    private Long id;

    private UserDto userDto;
    private NovelDto novelDto;

    private Integer score;
    private String body;

    private ReviewDto(Long id, UserDto userDto, NovelDto novelDto, Integer score, String body) {
        this.id = id;
        this.userDto = userDto;
        this.novelDto = novelDto;
        this.score = score;
        this.body = body;
    }

    public static ReviewDto from(Review entity){
        return new ReviewDto(
                entity.getId(),
                UserDto.from(entity.getUser()),
                NovelDto.from(entity.getNovel()),
                entity.getScore(),
                entity.getBody()
        );
    }
}
