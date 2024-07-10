package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.review.ReviewListResponse;
import com.novel.api.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public void write(WriteReviewRequest request, User user) {

    }

    public void delete(Long reviewId, User user) {

    }

    public PageingResponse<ReviewListResponse> getList(Long novelId) {

        return null;
    }
}
