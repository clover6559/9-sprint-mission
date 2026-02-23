package com.sprint.mission.discodeit.controller.Api;

import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusUpdate;
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
import org.springframework.http.ResponseEntity;

@Tag(name = "ReadStatus", description = "ReadStatus API")

public interface ReadStatusApi {

  @Operation(summary = "읽음 상태 생성")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201", description = "ReadStatus가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = "400", description = "ReadStatus가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "ReadStatus already exists"))
      ),
  })
  ResponseEntity<ReadStatus> create(
      @Parameter(description = "ReadStatus 생성 정보") ReadStatusCreate readStatusCreate
  );

  @Operation(summary = "읽음 상태 수정")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "ReadStatus 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = ReadStatus.class))
      ),
      @ApiResponse(
          responseCode = "404", description = "ReadStatus를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("ReadStatus with id {readStatusId} not found"))
      )
  })
  ResponseEntity<ReadStatus> update(
      @Parameter(description = "수정할 ReadStatus ID") UUID readStatusId,
      @Parameter(description = "수정할 ReadStatus 정보") ReadStatusUpdate readStatusUpdate
  );

  @Operation(summary = "사용자의 메세지 읽음 상태 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "사용자의 메세지 읽음 상태 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReadStatus.class)))
      )
  })
  ResponseEntity<List<ReadStatus>> findByUserId(
      @Parameter(description = "조회할 ReadStatus ID") UUID readStatusId
  );
}
