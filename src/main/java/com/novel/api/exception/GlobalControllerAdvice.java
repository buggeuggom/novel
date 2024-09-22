package com.novel.api.exception;

import com.novel.api.dto.response.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.novel.api.exception.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {
        log.trace("MethodArgumentNotValidException occurs {}", e.toString());
        var response = ErrorResponse.builder()
                .resultCode("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ExceptionHandler(NovelApplicationException.class)
    public ResponseEntity<ErrorResponse> novelApplicationExceptionHandler(NovelApplicationException e) {
        log.error("NovelApplicationException occurs {}", e.toString());
        var body = ErrorResponse.builder()
                .resultCode(e.getErrorCode().name())
                .message(e.getMessage())
                .build();

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        log.error("Error occurs {}", e.toString());

        return ResponseEntity.status(DATABASE_ERROR.getStatus())
                .body(ErrorResponse.builder()
                        .resultCode(DATABASE_ERROR.name())
                        .message(DATABASE_ERROR.getMessage()));
    }
}