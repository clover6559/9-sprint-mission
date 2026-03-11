package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "첨부 파일 정보")
public record BinaryContentDto(
    @Schema(description = "첨부 파일의 고유 식별자")
    UUID id,
    @Schema(description = "첨부 파일의 이름")
    String fileName,
    @Schema(description = "첨부 파일의 크기")
    Long size,
    @Schema(description = "첨부 파일의 타입")
    String contentType) {

}
