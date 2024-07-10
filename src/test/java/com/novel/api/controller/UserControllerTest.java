package com.novel.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel.api.dto.UserDto;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.exception.ErrorCode;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.UserFixture;
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

import static com.novel.api.exception.ErrorCode.DUPLICATED_USER_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UserService userService;

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

        when(userService.signup(any())).thenReturn(UserDto.from(UserFixture.get()));

        //expected
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("[signup][fail]: 중복된 user name")
    void signup_fail() throws Exception {
        //given
        UserSignupRequest request = UserSignupRequest.builder()
                .email("email")
                .name("name")
                .password("password")
                .build();

        when(userService.signup(any())).thenThrow(new NovelApplicationException(DUPLICATED_USER_NAME));

        //expected
        mockMvc.perform(post("/api/v1/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().is(DUPLICATED_USER_NAME.getStatus().value()));
    }
}