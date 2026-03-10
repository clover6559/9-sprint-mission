package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
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
  public ChannelDto createPublic(
      @RequestBody CreatePublicChannelRequest request
  ) {
    return channelService.create(request);
  }

  @PostMapping("/private")
  @Override
  public ChannelDto createPrivate(
      @RequestBody CreatePrivateChannelRequest request
  ) {
    return channelService.create(request);
  }

  @PatchMapping("/{channelId}")
  @Override
  public ChannelDto update(
      @PathVariable UUID channelId,
      @RequestBody ChannelUpdateRequest request
  ) {
    return channelService.update(channelId, request);
  }

  @DeleteMapping("/{channelId}")
  @Override
  public void delete(
      @PathVariable UUID channelId
  ) {
    channelService.delete(channelId);
  }

  @GetMapping
  @Override
  public List<ChannelDto> findAll(
      @RequestParam UUID userId
  ) {
    return channelService.findAllByUserId(userId);

  }
}
