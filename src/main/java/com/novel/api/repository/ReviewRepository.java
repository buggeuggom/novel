package com.novel.api.repository;

import com.novel.api.domain.review.Review;
import com.novel.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUser(User user);
}
