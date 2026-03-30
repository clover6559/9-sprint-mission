package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.*;

public record BinaryContentCreateRequest(
        @NotBlank(message = "파일 이름은 필수 입력 항목입니다.") String fileName,
        @NotBlank(message = "파일 타입(MIME type)은 필수 입력 항목입니다.") String contentType,
        @NotEmpty(message = "파일 데이터는 비어있을 수 없습니다.") byte[] bytes) {}
