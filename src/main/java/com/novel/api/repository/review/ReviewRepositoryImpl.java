package com.novel.api.repository.review;

import com.novel.api.domain.novel.QNovel;
import com.novel.api.domain.review.QReview;
import com.novel.api.domain.review.Review;
import com.novel.api.dto.request.review.ReviewSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static com.novel.api.domain.review.QReview.*;


@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Review> getList(Long novelId, ReviewSearch search) {

        long totalCount = jpaQueryFactory.select(review.count())
                .from(review)
                .fetchFirst();

        List<Review> reviews = jpaQueryFactory.selectFrom(review)
                .where(review.novel.id.eq(novelId))
                .orderBy(review.id.desc())
                .limit(search.getSize())
                .offset(search.getOffset())
                .fetch();

        return new PageImpl<>(reviews, search.getPageable(), totalCount);
    }
}
