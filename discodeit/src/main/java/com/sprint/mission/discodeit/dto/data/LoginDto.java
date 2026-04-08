package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 정보")
public record LoginDto(
        @Schema(description = "사용자 이름") String username, @Schema(description = "비밀번호") String password) {}
