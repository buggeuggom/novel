package com.novel.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.dto.request.review.WriteReviewRequest;
import com.novel.api.dto.response.PageingResponse;
import com.novel.api.fixture.ReviewFixture;
import com.novel.api.fixture.TestInfoFixture;
import com.novel.api.service.NovelService;
import com.novel.api.service.ReviewService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("ReviewControllerTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ReviewService reviewService;

    /**
     * write test
     */
    @Test
    @WithMockUser
    @DisplayName("[write][success]: ")
    void write_success() throws Exception {
        //given
        var info = TestInfoFixture.get();

        var request = WriteReviewRequest.builder()
                .score(1)
                .body("test content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/v1//novels/{novelId}/reviews", info.getNovelId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[write][fail]: forbidden")
    void write_fail_forbidden() throws Exception {
        //given
        var info = TestInfoFixture.get();

        var request = WriteReviewRequest.builder()
                .score(1)
                .body("test content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/api/v1//novels/{novelId}/reviews", info.getNovelId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    /**
     * delete test
     */
    @Test
    @WithMockUser
    @DisplayName("[delete][success]: ")
    void delete_success() throws Exception {
        //given
        var info = ReviewFixture.get();
        //when

        //expected
        mockMvc.perform(delete("/api/v1/reviews/{reviewId}", info.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[delete][fail]: forbidden <- 로그인 안된 유저")
    void delete_fail_forbidden() throws Exception {
        //given
        var info = ReviewFixture.get();
        //when

        //expected
        mockMvc.perform(delete("/api/v1/reviews/{reviewId}", info.getId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
    /**
     * getList
     */
    @Test
    @WithMockUser
    @DisplayName("[getList][success]: ")
    void getList_success() throws Exception {
        //given
        var info = TestInfoFixture.get();

        //when

        //expected
        mockMvc.perform(get("/api/v1/novels/{novelId}/reviews", info.getNovelId())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}