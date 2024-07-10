package com.novel.api.repository.review;

import com.novel.api.domain.review.Review;
import com.novel.api.dto.request.review.ReviewSearch;
import org.springframework.data.domain.Page;

public interface ReviewRepositoryCustom {

    Page<Review> getList(Long novelId, ReviewSearch search);
}
