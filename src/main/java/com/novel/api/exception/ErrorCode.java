package com.novel.api.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //401
    INVALID_EMAIL_OR_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid email or password"),
    INVALID_PERMISSION(UNAUTHORIZED, "User has invalid permission"),


    //404
    EPISODE_NOT_FOUND(NOT_FOUND, "Episode not founded"),
    NOVEL_NOT_FOUND(NOT_FOUND, "Novel not founded"),
    SUBSCRIPTION_NOT_FOUND(NOT_FOUND, "Subscription not founded"),
    USER_NOT_FOUND(NOT_FOUND, "User not founded"),


    //409
    ALREADY_SUBSCRIBED_NOVEL(CONFLICT, "User already subscribed novel"),
    DUPLICATED_NOVEL_NAME(CONFLICT, "Duplicated novel name"),
    DUPLICATED_USER(CONFLICT, "Duplicated user"),

    //500
    DATABASE_ERROR(INTERNAL_SERVER_ERROR, "Database error occurs"),
    ;

    private final HttpStatus status;
    private final String message;
}
