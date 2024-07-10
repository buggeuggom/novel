package com.novel.api.controller;

import com.novel.api.domain.user.User;
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

    @PostMapping()
    public void write(@RequestBody WriteReviewRequest request, Authentication authentication){
        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        reviewService.write(request, user);
    }

    @DeleteMapping("")
    public void delete(@PathVariable Long reviewId, Authentication authentication){
        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        reviewService.delete(reviewId, user);
    }

    @GetMapping
    public PageingResponse<ReviewListResponse> getList(@PathVariable Long novelId){
        return reviewService.getList(novelId);
    }
}
