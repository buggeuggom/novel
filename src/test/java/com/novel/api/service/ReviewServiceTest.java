package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.ReviewFixture;
import com.novel.api.repository.ReviewRepository;
import com.novel.api.repository.UserRepository;
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
@DisplayName("ReviewServiceTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ReviewServiceTest {


    @Autowired
    ReviewService reviewService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    ReviewRepository reviewRepository;

    /**
     * write test
     */

    @Test
    @DisplayName("[write][success]:")
    void write_success() {
        //given
        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findByUser(mockUser)).thenReturn(Optional.empty());

        //then
        assertDoesNotThrow(() -> reviewService.write(request, mockUser));
    }

    @Test
    @DisplayName("[write][fail]: ALREADY_REVIEW")
    void write_fail_already_review() {
        //given
        var fixture = ReviewFixture.get();

        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findByUser(mockUser)).thenReturn(Optional.of(fixture));

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.write(request, mockUser));
        assertEquals(ALREADY_REVIEW.getStatus().name(), e);
    }

    /**
     * delete test
     */
    @Test
    @DisplayName("[delete][success]:")
    void delete_success() {
        //given
        var info = ReviewFixture.get();
        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findById(info.getId())).thenReturn(Optional.of(info));
        when(info.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> reviewService.delete(info.getId(), mockUser));
    }

    @Test
    @DisplayName("[delete][fail]: REVIEW_NOT_FOUND")
    void delete_fail_review_not_found() {
        //given
        var info = ReviewFixture.get();
        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findById(info.getId())).thenReturn(Optional.empty());
        when(info.getUser()).thenReturn(mockUser);

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.delete(info.getId(), mockUser));
        assertEquals(REVIEW_NOT_FOUND.getStatus().name(), e);
    }

    @Test
    @DisplayName("[delete][fail]: INVALID_PERMISSION")
    void delete_fail_invalid_permission() {
        //given
        var info = ReviewFixture.get();
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findById(info.getId())).thenReturn(Optional.of(info));

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.delete(info.getId(), mockUser));
        assertEquals(REVIEW_NOT_FOUND.getStatus().name(), e);
    }

}