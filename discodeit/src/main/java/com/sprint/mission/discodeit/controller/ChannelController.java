package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserFind;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
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
  private final UserService userService;

  @Operation(summary = "공개 채널 생성")
  @PostMapping("/public")
  public ResponseEntity<Channel> createPublic(
      @RequestBody CreatePublicRequest request
  ) {
    UserFind user = userService.find(request.userId());
    CreatePublic createPublic = new CreatePublic(request.channelName(), request.description(),
        user);
    Channel channel = channelService.create(createPublic);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @Operation(summary = "비공개 채널 생성")
  @PostMapping("/private")
  public ResponseEntity<Channel> createPrivate(
      @RequestBody CreatePrivateRequest request
  ) {
    UserFind user = userService.find(request.userId());
    UUID creatorId = request.creatorId();

    CreatePrivate createPrivate = new CreatePrivate(creatorId, user);
    Channel channel = channelService.create(createPrivate);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @Operation(summary = "채널 정보 수정")
  @PatchMapping("/{channelId}")
  public ResponseEntity<Void> update(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdate.ChannelUpdateInfo channelUpdateInfo
  ) {
    ChannelUpdate channelUpdate = new ChannelUpdate(channelId, channelUpdateInfo);
    channelService.update(channelUpdate);
    return ResponseEntity.ok().build();
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
