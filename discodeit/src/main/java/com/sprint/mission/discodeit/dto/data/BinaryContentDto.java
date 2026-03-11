package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "첨부파일 정보")
public record BinaryContentDto(
    UUID id, String fileName, Long size, String contentType) {

}
