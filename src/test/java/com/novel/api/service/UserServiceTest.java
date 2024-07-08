package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.UserDto;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.fixture.UserFixture;
import com.novel.api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.novel.api.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("UserServiceTest")
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

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
        UserDto signup = userService.signup(request);

        //then
        assertEquals(user.getName(), signup.getName());
        assertEquals(user.getEmail(), signup.getEmail());
        assertTrue(passwordEncoder.matches(user.getPassword(), signup.getPassword()));
    }

    @Test
    @DisplayName("[signup][fail]")
    void signup_fail_by_existed_email() {
        //given
        User user = UserFixture.get();
        userRepository.save(user);

        UserSignupRequest request = UserSignupRequest.builder()
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
        //expected
        var e = assertThrows(NovelApplicationException.class, () -> userService.signup(request));
        assertEquals(e.getErrorCode(), DUPLICATED_USER_NAME);
    }


}