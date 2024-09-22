package com.novel.api.controller;

import com.novel.api.dto.request.UserSignupRequest;
import com.novel.api.dto.response.UserSignupResponse;
import com.novel.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public UserSignupResponse signup(@RequestBody UserSignupRequest request) {

        return UserSignupResponse.fromDto(userService.signup(request));
    }

}
