package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.security.UserDetailsImpl;
import com.novel.api.dto.UserDto;
import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.novel.api.domain.user.UserRole.READER;
import static com.novel.api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService  implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto signup(UserSignupRequest request) {

        userRepository.findByEmail(request.getEmail()).ifPresent(it -> {
            throw new NovelApplicationException(DUPLICATED_USER);
        });

        User user = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(READER)
                .build();

        return UserDto.from(userRepository.save(user));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User userData = userRepository.findByEmail(email)
                .orElseThrow(()->new NovelApplicationException(USER_NOT_FOUND));

        return new UserDetailsImpl(userData);

    }
}