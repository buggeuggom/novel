package com.novel.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.dto.request.review.WriteReviewRequest;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

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
     * post test
     */
    @Test
    @WithMockUser
    @DisplayName("[write][success]: ")
    void write_success () throws Exception {
        //given
        var request = WriteReviewRequest.builder()
                .score(1)
                .content("test content")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
//        mockMvc.perform(post("/api/v1//novels/{novelId}/reviews", ))

    }

}