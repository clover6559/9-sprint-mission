package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Channel API")
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;

  @Operation(summary = "공개 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(
      @RequestBody CreatePublic request
  ) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @Operation(summary = "비공개 채널 생성")
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @RequestBody CreatePrivate request
  ) {
    Channel channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @Operation(summary = "채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<Channel> update(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request
  ) {
    Channel channelUpdate = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelUpdate);
  }

  @Operation(summary = "채널 삭제")
  @DeleteMapping("/{channelId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "사용자가 참여 중인 채널 조회")
  @GetMapping
  public ResponseEntity<List<ChannelResponse>> findAll(
      @RequestParam UUID userId
  ) {
    List<ChannelResponse> channelResponse = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelResponse);

  }
}
