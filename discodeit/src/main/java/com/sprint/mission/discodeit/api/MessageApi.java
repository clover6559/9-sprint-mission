package com.sprint.mission.discodeit.api;

import static com.sprint.mission.discodeit.api.ApiDocsUtils.BAD_REQUEST_400;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.CREATED_201;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.NO_CONTENT_204;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageApi {

  @Operation(summary = "메세지 생성")
  @ResponseStatus(HttpStatus.CREATED)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = CREATED_201, description = "Message가 성공적으로 생성됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = BAD_REQUEST_400, description = "Message가 이미 존재함",
          content = @Content(examples = @ExampleObject(value = "Message already exists"))
      ),
  })
  MessageDto create(
      @Parameter(
          description = "Message 생성 정보",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
      ) MessageCreateRequest messageCreateRequest,
      @Parameter(
          description = "Message 첨부 파일",
          content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
      ) List<MultipartFile> attachmentRequests
  );

  @Operation(summary = "메세지 내용 수정")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "Message 정보가 성공적으로 수정됨",
          content = @Content(schema = @Schema(implementation = Message.class))
      ),
      @ApiResponse(
          responseCode = NOT_FOUND_404, description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject("Message with id {messageId} not found"))
      )
  })
  MessageDto update(
      @Parameter(description = "수정할 Message ID") UUID messageId,
      @Parameter(description = "수정할 Message 정보") MessageUpdateRequest messageUpdateRequest
  );


  @Operation(summary = "메세지 삭제")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = NO_CONTENT_204, description = "Message가 성공적으로 삭제됨"
      ),
      @ApiResponse(
          responseCode = NOT_FOUND_404, description = "Message를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "Message with id {id} not found"))
      )
  })
  void delete(
      @Parameter(description = "삭제할 Message ID") UUID messageId
  );

  @ApiResponses(value = {
      @ApiResponse(
          responseCode = SUCCESS_200, description = "Message 목록 조회 성공",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = Message.class)))
      )
  })
  @Operation(summary = "해당 채널의 메세지 조회")
  @ResponseStatus(HttpStatus.OK)
  PageResponse<MessageDto> findByChannelId(
      @Parameter(description = "조회할 Channel ID") UUID channelId,
      @Parameter(description = "조회할 Channel ID") Instant cursor,
      @ParameterObject Pageable pageable
  );

}
