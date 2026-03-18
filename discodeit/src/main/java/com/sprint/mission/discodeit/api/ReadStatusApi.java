package com.sprint.mission.discodeit.api;

import static com.sprint.mission.discodeit.api.ApiDocsUtils.BAD_REQUEST_400;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.CREATED_201;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "ReadStatus", description = "ReadStatus API")

public interface ReadStatusApi {

  @Operation(summary = "읽음 상태 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = CREATED_201, description = "ReadStatus가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = BAD_REQUEST_400, description = "ReadStatus가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "ReadStatus already exists"))
      ),
  })
  ReadStatusDto create(
      @Parameter(description = "ReadStatus 생성 정보") ReadStatusCreateRequest readStatusCreateRequest
  );

  @Operation(summary = "읽음 상태 수정")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "ReadStatus 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = NOT_FOUND_404, description = "ReadStatus를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("ReadStatus with id {readStatusId} not found"))
      )
  })
  ReadStatusDto update(
      @Parameter(description = "수정할 ReadStatus ID") UUID readStatusId,
      @Parameter(description = "수정할 ReadStatus 정보") ReadStatusUpdateRequest readStatusUpdateRequest
  );

  @Operation(summary = "사용자의 메세지 읽음 상태 조회")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "사용자의 메세지 읽음 상태 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))
      )
  })
  List<ReadStatusDto> findByUserId(
      @Parameter(description = "조회할 ReadStatus ID") UUID readStatusId
  );
}
