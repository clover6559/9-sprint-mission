package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivate;
import com.sprint.mission.discodeit.dto.request.CreatePublic;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PostMapping("/public")
  @Override
  public ResponseEntity<ChannelDto> createPublic(
      @RequestBody CreatePublic request
  ) {
    ChannelDto channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @PostMapping("/private")
  @Override
  public ResponseEntity<ChannelDto> createPrivate(
      @RequestBody CreatePrivate request
  ) {
    ChannelDto channel = channelService.create(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(channel);
  }

  @PatchMapping("/{channelId}")
  @Override
  public ResponseEntity<ChannelDto> update(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request
  ) {
    ChannelDto channelUpdate = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channelUpdate);
  }

  @DeleteMapping("/{channelId}")
  @Override
  public ResponseEntity<Void> delete(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<ChannelDto>> findAll(
      @RequestParam UUID userId
  ) {
    List<ChannelDto> channelDto = channelService.findAllByUserId(userId);
    return ResponseEntity.ok(channelDto);

  }
}
