package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.UserDto;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.exception.ErrorCode;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.novel.api.domain.user.UserRole.READER;
import static com.novel.api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto signup(UserSignupRequest request) {

        //check name is not exist
        userRepository.findByName(request.getName()).ifPresent(it -> {
            throw new NovelApplicationException(DUPLICATED_USER_NAME);
        });

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(READER)
                .build();

        return UserDto.fromEntity(userRepository.save(user));
    }
}