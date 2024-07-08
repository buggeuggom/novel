package com.novel.api.fixture;

import com.novel.api.domain.user.User;
import com.novel.api.domain.user.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserFixture {

    public static User get(String name, String password) {
        return User.builder()
                .name(name)
                .email("test@email.com")
                .password(password)
                .userRole(UserRole.READER)
                .build();
    }

    public static User get() {
        return User.builder()
                .name("testName")
                .email("test@email.com")
                .password("testPassword")
                .userRole(UserRole.READER)
                .build();
    }

    public static User get(PasswordEncoder passwordEncoder) {
        return User.builder()
                .name("testName")
                .email("test@email.com")
                .password(passwordEncoder.encode("testPassword"))
                .userRole(UserRole.READER)
                .build();
    }
}
