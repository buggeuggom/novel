package com.novel.api.controller;

import com.novel.api.service.SubscriptionService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SubscriptionControllerTest")
@TestMethodOrder(MethodOrderer.MethodName.class)
class SubscriptionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SubscriptionService subscriptionService;

    /**
     * subscribe test
     */
    @Test
    @WithMockUser
    @DisplayName("[subscribe][success]")
    void subscribe_success() throws Exception {
        //given
        var novelId = 301L;
        //when

        //then
        mockMvc.perform(post("/api/v1/novels/{novelId}/subscribes", novelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[subscribe][fail]: forbidden")
    void subscribe_fail_forbidden() throws Exception {
        //given
        var novelId = 301L;
        //when

        //then
        mockMvc.perform(post("/api/v1/novels/{novelId}/subscribes", novelId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    /**
     * unsubscribe test
     */
    @Test
    @WithMockUser
    @DisplayName("[unsubscribe][success]")
    void unsubscribe_success() throws Exception {
        //given
        var subscribeId = 401L;

        //when
        doNothing().when(subscriptionService).unsubscribe(eq(subscribeId), any());

        //then
        mockMvc.perform(delete("/api/v1/subscribes/{subscribeId}", subscribeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[unsubscribe][fail]: forbidden")
    void unsubscribe_fail_forbidden() throws Exception {
        //given
        var subscribeId = 401L;

        //then
        mockMvc.perform(delete("/api/v1/subscribes/{subscribeId}", subscribeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}