package com.novel.api.service;

import com.novel.api.domain.novel.Novel;
import com.novel.api.domain.subscription.Subscription;
import com.novel.api.domain.user.User;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.UserRepository;
import com.novel.api.repository.novel.NovelRepository;
import com.novel.api.repository.subscription.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static com.novel.api.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("SubscriptionServiceTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class SubscriptionServiceTest {

    @Autowired
    SubscriptionService subscriptionService;

    @MockBean
    SubscriptionRepository subscriptionRepository;
    @MockBean
    NovelRepository novelRepository;
    @MockBean
    UserRepository userRepository;

    /**
     * subscribe test
     */
    @Test
    @DisplayName("[subscribe][success]:")
    void subscribe_success() {
        //given
        User user = mock(User.class);
        Novel novel = mock(Novel.class);

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(subscriptionRepository.findByNovelAndUser(novel, user)).thenReturn(Optional.empty());

        //then
        assertDoesNotThrow(()-> subscriptionService.subscribe(novel.getId(), user));
    }

    @Test
    @DisplayName("[subscribe][fail]: NOVEL_NOT_FOUND")
    void subscribe_fail_novel_not_found() {
        //given
        User user = mock(User.class);
        Novel novel = mock(Novel.class);

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.empty());

        //then
        var e = assertThrows(NovelApplicationException.class, () -> subscriptionService.subscribe(novel.getId(), user));
        assertEquals(NOVEL_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("[subscribe][fail]: ALREADY_SUBSCRIBED_NOVEL")
    void subscribe_fail_already_subscribed_novel() {
        //given
        User user = mock(User.class);
        Novel novel = mock(Novel.class);
        Subscription subscription = mock(Subscription.class);

        //when
        when(novelRepository.findById(novel.getId())).thenReturn(Optional.of(novel));
        when(subscriptionRepository.findByNovelAndUser(novel, user)).thenReturn(Optional.of(subscription));

        //then
        var e = assertThrows(NovelApplicationException.class, () -> subscriptionService.subscribe(novel.getId(), user));
        assertEquals(ALREADY_SUBSCRIBED_NOVEL, e.getErrorCode());
    }

    /**
     * unsubscribe test
     */

    @Test
    @DisplayName("[unsubscribe][success]:")
    void unsubscribe_success() {
        //given
        Subscription subscription = mock(Subscription.class);
        User user = mock(User.class);

        //when
        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.of(subscription));
        when(subscription.getUser()).thenReturn(user);

        //then
        assertDoesNotThrow(()-> subscriptionService.unsubscribe(subscription.getId(), user));
    }


    @Test
    @DisplayName("[unsubscribe][fail]: SUBSCRIPTION_NOT_FOUND")
    void unsubscribe_fail_subscription_not_found() {
        //given
        Subscription subscription = mock(Subscription.class);
        User user = mock(User.class);

        //when
        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.empty());

        //then
        var e = assertThrows(NovelApplicationException.class, ()-> subscriptionService.unsubscribe(subscription.getId(), user));
        assertEquals(SUBSCRIPTION_NOT_FOUND, e.getErrorCode());
    }


    @Test
    @DisplayName("[unsubscribe][fail]: INVALID_PERMISSION")
    void unsubscribe_fail_invalid_permission() {
        //given
        Subscription subscription = mock(Subscription.class);
        User user = mock(User.class);

        //when
        when(subscriptionRepository.findById(subscription.getId())).thenReturn(Optional.of(subscription));
        when(subscription.getUser()).thenReturn(mock(User.class));

        //then
        var e = assertThrows(NovelApplicationException.class, ()-> subscriptionService.unsubscribe(subscription.getId(), user));
        assertEquals(INVALID_PERMISSION, e.getErrorCode());
    }

}