package com.sprint.mission.discodeit.api;

import static com.sprint.mission.discodeit.api.ApiDocsUtils.BAD_REQUEST_400;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.CREATED_201;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.NOT_FOUND_404;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.NO_CONTENT_204;
import static com.sprint.mission.discodeit.api.ApiDocsUtils.SUCCESS_200;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.entity.Channel;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApi {

    @Operation(summary = "공개 채널 생성")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = CREATED_201,
                        description = "채널이 성공적으로 생성됨",
                        content = @Content(schema = @Schema(implementation = Channel.class))),
                @ApiResponse(
                        responseCode = BAD_REQUEST_400,
                        description = "같은 채널이 이미 존재함",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(value = "Channel already exists"))),
            })
    ResponseEntity<ChannelDto> createPublic(
            @Parameter(
                            description = "Channel 생성 정보",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                    CreatePublicChannelRequest request);

    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = CREATED_201,
                        description = "채널이 성공적으로 생성됨",
                        content = @Content(schema = @Schema(implementation = Channel.class))),
                @ApiResponse(
                        responseCode = BAD_REQUEST_400,
                        description = "같은 채널이 이미 존재함",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(value = "Channel already exists"))),
            })
    @Operation(summary = "비공개 채널 생성")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<ChannelDto> createPrivate(
            @Parameter(
                            description = "Channel 생성 정보",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
                    CreatePrivateChannelRequest request);

    @Operation(summary = "채널 정보 수정")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = SUCCESS_200,
                        description = "Channel 정보가 성공적으로 수정됨",
                        content = @Content(schema = @Schema(implementation = Channel.class))),
                @ApiResponse(
                        responseCode = NOT_FOUND_404,
                        description = "Channel을 찾을 수 없음",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        "Channel with id {channelId} not found"))),
                @ApiResponse(
                        responseCode = BAD_REQUEST_400,
                        description = "같은 name를 사용하는 Channel을 이미 존재함",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        "channel을 with name {newname} already exists")))
            })
    ResponseEntity<ChannelDto> update(
            @Parameter(description = "수정할 Channel ID") UUID channelId,
            @Parameter(description = "수정할 Channel 정보") ChannelUpdateRequest request);

    @Operation(summary = "채널 삭제")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = NO_CONTENT_204, description = "Channel이 성공적으로 삭제됨"),
                @ApiResponse(
                        responseCode = NOT_FOUND_404,
                        description = "Channel을 찾을 수 없음",
                        content =
                                @Content(
                                        examples =
                                                @ExampleObject(
                                                        value = "Channel with id {id} not found")))
            })
    ResponseEntity<Void> delete(@Parameter(description = "삭제할 Channel ID") UUID channelId);

    @Operation(summary = "사용자가 참여 중인 채널 조회")
    @ResponseStatus(HttpStatus.OK)
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = SUCCESS_200,
                        description = "사용자가 참여중인 채널 목록 조회 성공",
                        content =
                                @Content(
                                        array =
                                                @ArraySchema(
                                                        schema =
                                                                @Schema(
                                                                        implementation =
                                                                                ChannelDto.class))))
            })
    ResponseEntity<List<ChannelDto>> findAll(@Parameter(description = "조회할 User ID") UUID userId);
}
