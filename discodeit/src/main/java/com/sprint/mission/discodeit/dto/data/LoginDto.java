package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 로그인 요청 정보")
public record LoginDto(String username, String password) {

}
