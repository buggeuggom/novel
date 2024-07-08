package com.novel.api.dto.response.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String resultCode;
    private final String message;

    @Builder
    private ErrorResponse(String resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }
}
