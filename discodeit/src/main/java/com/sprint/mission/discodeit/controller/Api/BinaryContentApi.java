package com.sprint.mission.discodeit.controller.Api;

import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

@Tag(name = "BinaryContent", description = "BinaryContent API")
public interface BinaryContentApi {

  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "여러 첨부파일 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
      )
  })
  ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @Parameter(description = "조회할 BinaryContent Ids") List<UUID> binaryContentIds
  );

  @Operation(summary = "특정 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "특정 첨부파일 조회 성공",
          content = @Content(schema = @Schema(implementation = BinaryContent.class))
      ),
      @ApiResponse(responseCode = "404", description = "파일을 찾을 수 없음")
  })
  ResponseEntity<BinaryContent> find(
      @Parameter(description = "조회할 BinaryContent ID") UUID binaryContentId
  );
}
