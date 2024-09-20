package com.novel.api.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginResponse {
    private final String token;
    private final String name;

    @Builder
    private LoginResponse(String token, String name) {
        this.token = token;
        this.name = name;
    }
}
