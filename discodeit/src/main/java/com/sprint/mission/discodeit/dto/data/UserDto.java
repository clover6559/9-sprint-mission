package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "유저 정보")
public record UserDto(
    @Schema(description = "유저의 고유 식별자")
    UUID id,
    @Schema(description = "유저의 이름")
    String username,
    @Schema(description = "유저의 이메일")
    String email,
    @Schema(description = "유저의 프로필 이미지")
    BinaryContentDto profile,
    @Schema(description = "유저의 온라인 상태")
    Boolean online
) {

}
