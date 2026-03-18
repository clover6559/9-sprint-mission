package com.sprint.mission.discodeit.api;

import static com.sprint.mission.discodeit.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "BinaryContent", description = "BinaryContent API")
public interface BinaryContentApi {

  @Operation(summary = "여러 첨부 파일 조회")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "여러 첨부파일 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
      ),
      @ApiResponse(
          responseCode = NOT_FOUND_404, description = "첨부 파일을 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
      )
  })
  List<BinaryContentDto> findAllByIdIn(
      @Parameter(description = "조회할 BinaryContent Ids") List<UUID> binaryContentIds
  );

  @Operation(summary = "특정 첨부 파일 조회")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "특정 첨부파일 조회 성공",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      ),
      @ApiResponse(responseCode = NOT_FOUND_404, description = "파일을 찾을 수 없음")
  })
  BinaryContentDto find(
      @Parameter(description = "조회할 BinaryContent ID") UUID binaryContentId
  );

  @Operation(summary = "특정 파일 다운")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "특정 첨부파일 다운 성공",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      )
  })
  ResponseEntity<?> download(
      @Parameter(description = "다운할 BinaryContent ID") UUID binaryContentId
  );
}
