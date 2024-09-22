package com.novel.api.fixture;

import com.novel.api.domain.user.User;
import com.novel.api.domain.user.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFixture {

    public static User get() {
        return User.builder()
                .id(1L)
                .name("testName")
                .email("test@email.com")
                .password("testPassword")
                .build();
    }

    public static User get(String name, String password) {
        return User.builder()
                .id(1L)
                .name(name)
                .email("test@email.com")
                .password(password)
                .build();
    }

    public static User get(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name("testName")
                .email("test@email.com")
                .password(passwordEncoder.encode("testPassword"))
                .build();
    }
}
