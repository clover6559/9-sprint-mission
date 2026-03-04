package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.data.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(NoSuchElementException.class)
  public ResponseEntity<ErrorResponse> handleNoSuchElement(NoSuchElementException e) {
    System.out.println("[Error] 데이터 없음: " + e.getMessage());
    ErrorResponse response = new ErrorResponse(e.getMessage(), 404);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException e) {
    System.out.println("[Error] 잘못된 인자: " + e.getMessage());
    ErrorResponse response = new ErrorResponse(e.getMessage(), 400);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleFileSizeLimit(MaxUploadSizeExceededException e) {
    System.out.println("[Error] 파일 크기가 큽니다. 제한 용량을 확인해주세요.");
    ErrorResponse response = new ErrorResponse(e.getMessage(), 413);
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorResponse> handleRunTime(RuntimeException e) {
    System.out.println("[Error] 알 수 없는 서버 오류: " + e.getMessage());
    ErrorResponse response = new ErrorResponse(e.getMessage(), 500);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}


