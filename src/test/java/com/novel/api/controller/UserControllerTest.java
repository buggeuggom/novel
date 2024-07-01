package com.novel.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.dto.UserDto;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserController")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 회원 가입
     */
    @Test
    @WithAnonymousUser
    @DisplayName("[signup][success]")
    void signup_success() throws Exception {
        //given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("email")
                .name("name")
                .password("password")
                .build();

        when(userService.signup(request)).thenReturn(mock(UserDto.class));

        //expected
        mockMvc.perform(post("/api/v1/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("[signup][fail]: 중복된 user name")
    void signup_fail() throws Exception {
        //given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("email")
                .name("name")
                .password("password")
                .build();

        when(userService.signup(request)).thenThrow(new RuntimeException());

        //expected
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}