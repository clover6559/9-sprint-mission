package com.sprint.mission.discodeit.controller.api;

import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.BAD_REQUEST_400;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.controller.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.LoginDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Auth", description = "Auth API")
public interface AuthApi {

  @Operation(summary = "로그인")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = SUCCESS_200,
              description = "로그인 성공",
              content = @Content(schema = @Schema(implementation = User.class))),
          @ApiResponse(
              responseCode = NOT_FOUND_404,
              description = "사용자를 찾을 수 없음",
              content =
              @Content(examples = @ExampleObject(value = "User with username {username} not found"))),
          @ApiResponse(
              responseCode = BAD_REQUEST_400,
              description = "비밀번호가 일치하지 않음",
              content = @Content(examples = @ExampleObject(value = "Wrong password")))
      })
  ResponseEntity<UserDto> login(@Parameter(description = "로그인") LoginDto loginDto);
}
