package com.novel.api.dto.response.error;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ErrorResponse {
    private final String resultCode;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String resultCode, String message, Map<String, String> validation) {
        this.resultCode = resultCode;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }

}
