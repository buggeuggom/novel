package com.novel.api.controller;

import com.novel.api.domain.user.User;
import com.novel.api.service.SubscriptionService;
import com.novel.api.utils.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/novels/{novelId}/subscribes")
    public void subscribe(@PathVariable Long novelId, Authentication authentication){

        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        subscriptionService.subscribe(novelId, user);
    }

    @DeleteMapping("/subscribes/{subscribeId}")
    public void unsubscribe(@PathVariable Long subscribeId, Authentication authentication){

        User user = ClassUtils.getSafeUserBySafeCast(authentication);

        subscriptionService.unsubscribe(subscribeId, user);
    }
}
