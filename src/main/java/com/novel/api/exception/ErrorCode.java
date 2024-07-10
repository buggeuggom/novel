package com.novel.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //401
    INVALID_PERMISSION(UNAUTHORIZED, "User has invalid permission"),

    //404
    EPISODE_NOT_FOUND(NOT_FOUND, "Episode not founded"),
    REVIEW_NOT_FOUND(NOT_FOUND, "Review not founded "),
    NOVEL_NOT_FOUND(NOT_FOUND, "Novel not founded"),
    USER_NOT_FOUND(NOT_FOUND, "User not founded"),


    //409
    ALREADY_REVIEW(CONFLICT, "User already wrote review of this novel"),
    DUPLICATED_NOVEL_NAME(CONFLICT, "Duplicated novel name"),
    DUPLICATED_USER_NAME(CONFLICT, "Duplicated user name"),

    //500
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "Database error occurs"),
    ;

    private final HttpStatus status;
    private final String message;
}
