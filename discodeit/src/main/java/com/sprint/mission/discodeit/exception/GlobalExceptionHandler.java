package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DiscodeitException.class)
    public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
        log.warn("비즈니스 예외 발생 - 코드: {}, 메시지: {}", e.getErrorCode(), e.getMessage());
        HttpStatus status = determineStatus(e.getErrorCode());
        ErrorResponse response = new ErrorResponse(
                e.getTimestamp(),
                e.getErrorCode().name(),
                e.getMessage(),
                e.getDetails(),
                e.getClass().getSimpleName(),
                status.value());
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        log.error("예상치 못한 시스템 오류 발생: ", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                ErrorCode.INTERNAL_SERVER_ERROR.name(),
                "서버 내부에서 알 수 없는 오류가 발생했습니다.",
                null,
                e.getClass().getSimpleName(),
                status.value());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        Map<String, Object> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                "INVALID_INPUT",
                "입력값 검증에 실패했습니다.",
                errors,
                e.getClass().getSimpleName(),
                HttpStatus.BAD_REQUEST.value());

        return ResponseEntity.badRequest().body(response);
    }

    private HttpStatus determineStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATE_USER, PRIVATE_CHANNEL_UPDATE -> HttpStatus.BAD_REQUEST;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FILE_UPLOAD_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
