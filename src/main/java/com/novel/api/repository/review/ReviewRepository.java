package com.novel.api.repository.review;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.review.Review;
import com.novel.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByUser(User user);

    Optional<Review> findByNovelAndUser(Novel novel, User user);
}
