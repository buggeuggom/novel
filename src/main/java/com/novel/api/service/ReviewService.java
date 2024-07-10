package com.novel.api.service;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.review.Review;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.review.ReviewSearch;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.episode.EpisodeListResponse;
import com.novel.api.dto.response.review.ReviewListResponse;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.review.ReviewRepository;
import com.novel.api.repository.novel.NovelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.novel.api.exception.ErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final NovelRepository novelRepository;
    private final ReviewRepository reviewRepository;

    public void write(Long novelId, WriteReviewRequest request, User user) {
        //find novel
        Novel novel = novelRepository.findById(novelId)
                .orElseThrow(() -> new NovelApplicationException(NOVEL_NOT_FOUND));

        //사용자가 리뷰를 작성했는지 확인
        reviewRepository.findByNovelAndUser(novel, user).ifPresent(it -> {
            new NovelApplicationException(ALREADY_REVIEW_WROTE);
        });

        Review review = Review.builder()
                .user(user)
                .novel(novel)
                .score(request.getScore())
                .body(request.getBody())
                .build();

        reviewRepository.save(review);
    }

    public void delete(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NovelApplicationException(REVIEW_NOT_FOUND));

        if (!review.getUser().equals(user)){
            throw new NovelApplicationException(INVALID_PERMISSION);
        }

        reviewRepository.delete(review);
    }

    public PageingResponse<ReviewListResponse> getList(Long novelId, ReviewSearch search) {
        Page<Review> reviews = reviewRepository.getList(novelId, search);

        return new PageingResponse<>(reviews, ReviewListResponse.class);
    }
}
