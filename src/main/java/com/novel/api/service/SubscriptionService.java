package com.novel.api.service;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.subscription.Subscription;
import com.novel.api.domain.user.User;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.novel.NovelRepository;
import com.novel.api.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.novel.api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final NovelRepository novelRepository;


    public void subscribe(Long novelId, User user) {
        Novel novel = novelRepository.findById(novelId).orElseThrow(() -> new NovelApplicationException(NOVEL_NOT_FOUND));

        subscriptionRepository.findByNovelAndUser(novel, user).ifPresent(it -> {
            throw new NovelApplicationException(ALREADY_SUBSCRIBED_NOVEL);
        });


        var subscription = Subscription.builder().novel(novel).user(user).build();

        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(Long subscribeId, User user) {

        Subscription subscription = subscriptionRepository.findById(subscribeId).orElseThrow(() -> new NovelApplicationException(SUBSCRIPTION_NOT_FOUND));

        if (!subscription.getUser().equals(user)) throw new NovelApplicationException(INVALID_PERMISSION);

        subscriptionRepository.delete(subscription);
    }


}
