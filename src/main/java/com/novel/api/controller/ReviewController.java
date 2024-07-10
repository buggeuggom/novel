package com.novel.api.controller;

import com.novel.api.domain.user.User;
import com.novel.api.dto.request.review.ReviewSearch;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.dto.response.review.ReviewListResponse;
import com.novel.api.service.ReviewService;
import com.novel.api.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/novels/{novelId}/reviews")
    public void write(@PathVariable Long novelId, @RequestBody WriteReviewRequest request, Authentication authentication) {
        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        reviewService.write(novelId, request, user);
    }

    @DeleteMapping("/reviews/{reviewId}")
    public void delete(@PathVariable Long reviewId, Authentication authentication) {
        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        reviewService.delete(reviewId, user);
    }

    @GetMapping("/novels/{novelId}/reviews")
    public PageingResponse<ReviewListResponse> getList(@PathVariable Long novelId, @ModelAttribute ReviewSearch search) {

        return reviewService.getList(novelId, search);
    }
}
