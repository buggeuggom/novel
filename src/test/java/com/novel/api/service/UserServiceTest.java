package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.UserFixture;
import com.novel.api.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.novel.api.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserServiceTest")
class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userRepository;
    @MockBean
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("[signup][success]")
    void signup_success() {
        //given
        User user = UserFixture.get();

        UserSignupRequest request = UserSignupRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        //when
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);

        //then
        assertDoesNotThrow(()-> userService.signup(request));
    }

    @Test
    @DisplayName("[signup][fail]")
    void signup_fail_by_existed_email() {
        //given
        User user = UserFixture.get();

        UserSignupRequest request = UserSignupRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        //when
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        //then
        var e = assertThrows(NovelApplicationException.class, () -> userService.signup(request));
        assertEquals(e.getErrorCode(), DUPLICATED_USER);
    }


}