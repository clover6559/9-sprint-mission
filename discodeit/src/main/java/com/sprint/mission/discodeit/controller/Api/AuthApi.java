package com.sprint.mission.discodeit.controller.Api;

import com.sprint.mission.discodeit.dto.LoginDto;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Auth API")

public interface AuthApi {

  @Operation(summary = "로그인")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "로그인 성공",
          content = @Content(schema = @Schema(implementation = LoginDto.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "잘못된 입력 형식",
          content = @Content(examples = @ExampleObject("Email is required"))
      ),
      @ApiResponse(
          responseCode = "401", description = "잘못된 아이디 또는 비밀번호",
          content = @Content(examples = @ExampleObject(value = "Invalid name or password"))
      )
  })
  ResponseEntity<User> login(
      @Parameter(description = "로그인") LoginDto loginDto
  );

}
