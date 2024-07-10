package com.novel.api.service;

import com.novel.api.domain.review.Review;
import com.novel.api.domain.user.User;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.ReviewFixture;
import com.novel.api.repository.review.ReviewRepository;
import com.novel.api.repository.UserRepository;
import com.novel.api.repository.novel.NovelRepository;
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
    @MockBean
    NovelRepository novelRepository;

    /**
     * write test
     */
    @Test
    @DisplayName("[write][success]:")
    void write_success() {
        //given
        var info = ReviewFixture.get();

        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(info.getNovel().getId())).thenReturn(Optional.of(info.getNovel()));
        when(reviewRepository.findByNovelAndUser(info.getNovel(), mockUser)).thenReturn(Optional.empty());

        //then
        assertDoesNotThrow(() -> reviewService.write(info.getNovel().getId(), request, mockUser));
    }

    @Test
    @DisplayName("[write][fail]: NOVEL_NOT_FOUND")
    void write_fail_novel_not_found() {
        //given
        var info = ReviewFixture.get();

        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(info.getNovel().getId())).thenReturn(Optional.empty());
        when(reviewRepository.findByNovelAndUser(info.getNovel(), mockUser)).thenReturn(Optional.empty());

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.write(info.getNovel().getId(), request, mockUser));
        assertEquals(NOVEL_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("[write][fail]: ALREADY_REVIEW")
    void write_fail_already_review() {
        //given
        var info = ReviewFixture.get();

        var request = mock(WriteReviewRequest.class);
        User mockUser = mock(User.class);

        //when
        when(novelRepository.findById(info.getNovel().getId())).thenReturn(Optional.of(info.getNovel()));
        when(reviewRepository.findByNovelAndUser(info.getNovel(), mockUser)).thenReturn(Optional.of(info));

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.write(info.getNovel().getId(), request, mockUser));
        assertEquals(ALREADY_REVIEW_WROTE, e.getErrorCode());
    }

    /**
     * delete test
     */
    @Test
    @DisplayName("[delete][success]:")
    void delete_success() {
        //given
        Review mockReview = mock(Review.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findById(mockReview.getId())).thenReturn(Optional.of(mockReview));
        when(mockReview.getUser()).thenReturn(mockUser);

        //then
        assertDoesNotThrow(() -> reviewService.delete(mockReview.getId(), mockUser));
    }

    @Test
    @DisplayName("[delete][fail]: REVIEW_NOT_FOUND")
    void delete_fail_review_not_found() {
        //given
        Review mockReview = mock(Review.class);
        User mockUser = mock(User.class);

        //when
        when(reviewRepository.findById(mockReview.getId())).thenReturn(Optional.empty());
        when(mockReview.getUser()).thenReturn(mockUser);

        //then
        var e = assertThrows(NovelApplicationException.class, () -> reviewService.delete(mockReview.getId(), mockUser));
        assertEquals(REVIEW_NOT_FOUND, e.getErrorCode());
    }

}