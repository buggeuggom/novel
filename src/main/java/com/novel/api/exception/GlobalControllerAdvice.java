package com.novel.api.exception;

import com.novel.api.dto.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.novel.api.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(NovelApplicationException.class)
    public ResponseEntity<ErrorResponse> errorHandler(NovelApplicationException e) {
        log.error("Error occurs {}", e.toString());
        var body = ErrorResponse.builder()
                .resultCode(e.getErrorCode().name())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> databaseErrorHandler(IllegalArgumentException e) {
        log.error("Error occurs {}", e.toString());

        return ResponseEntity.status(DATABASE_ERROR.getStatus())
                .body(ErrorResponse.builder()
                        .resultCode(DATABASE_ERROR.name())
                        .message(DATABASE_ERROR.getMessage()));
    }
}