package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Schema(description = "메시지 정보")
public record MessageDto(
        @Schema(description = "메시지의 고유 식별자") UUID id,
        @Schema(description = "메시지의 생성일시") Instant createdAt,
        @Schema(description = "메시지의 수정일시") Instant updatedAt,
        @Schema(description = "메시지의 본문 내용") String content,
        @Schema(description = "메시지가 속한 채널 ID") UUID channelId,
        @Schema(description = "메시지의 작성자 정보") UserDto author,
        @Schema(description = "첨부 파일 리스트") List<BinaryContentDto> attachments) {}
