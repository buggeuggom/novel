package com.novel.api.repository.subscription;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.subscription.Subscription;
import com.novel.api.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByNovelAndUser(Novel novel, User user);
}
