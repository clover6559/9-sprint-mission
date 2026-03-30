package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.service.ChannelService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController implements ChannelApi {

    private final ChannelService channelService;

    @PostMapping("/public")
    @Override
    public ResponseEntity<ChannelDto> createPublic(
            @Valid @RequestBody CreatePublicChannelRequest request) {
        log.info("공개 채널 생성 요청 수신 - 채널 이름:{}, 채널 설명: {}", request.name(), request.description());
        ChannelDto createdChannel = channelService.create(request);
        log.info(
                "채널 생성 요청 처리 완료 - 채널 ID: {}, 채널 이름: {}",
                createdChannel.id(),
                createdChannel.name());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @PostMapping("/private")
    @Override
    public ResponseEntity<ChannelDto> createPrivate(
            @Valid @RequestBody CreatePrivateChannelRequest request) {
        log.info("비공개 채널 생성 요청 수신 - 참여자 수: {}", request.participantIds().size());
        log.debug("채널 참여자 ID 목록: {}", request.participantIds());
        ChannelDto createdChannel = channelService.create(request);
        log.info("채널 생성 요청 처리 완료 - 채널 ID: {}", createdChannel.id());

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @PatchMapping("/{channelId}")
    @Override
    public ResponseEntity<ChannelDto> update(
            @PathVariable UUID channelId, @Valid @RequestBody ChannelUpdateRequest request) {
        log.info(
                "채널 업데이트 요청 수신 - 채널 ID: {}, 변경할 이름: {}, 변경할 내용: {}",
                channelId,
                request.newName(),
                request.newDescription());
        ChannelDto updatedChannel = channelService.update(channelId, request);
        log.info("채널 업데이트 요청 처리 완료 - 채널 ID: {}", updatedChannel.id());

        return ResponseEntity.status(HttpStatus.OK).body(updatedChannel);
    }

    @DeleteMapping("/{channelId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID channelId) {
        log.info("채널 삭제 요청 수신 - 채널 ID: {}", channelId);
        channelService.delete(channelId);
        log.info("채널 삭제 요청 처리 완료 - 채널 ID: {}", channelId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @Override
    public ResponseEntity<List<ChannelDto>> findAll(@RequestParam UUID userId) {
        List<ChannelDto> channels = channelService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(channels);
    }
}
