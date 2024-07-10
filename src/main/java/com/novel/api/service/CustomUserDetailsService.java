package com.novel.api.service;

import com.novel.api.domain.user.User;
import com.novel.api.dto.CustomUserDetails;
import com.novel.api.exception.NovelApplicationException;
import com.novel.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.novel.api.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User userData = userRepository.findByName(username).orElseThrow(()->new NovelApplicationException(USER_NOT_FOUND));

        return new CustomUserDetails(userData);
    }
}
