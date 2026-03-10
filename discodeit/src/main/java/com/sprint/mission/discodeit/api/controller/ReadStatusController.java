package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequestMapping("/api/readStatuses")
@RestController
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @PostMapping
  @Override
  public ReadStatusDto create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    return readStatusService.create(readStatusCreateRequest);
  }

  @Operation(summary = "읽음 상태 수정")
  @PatchMapping("/{readStatusId}")
  @Override
  public ReadStatusDto update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
  ) {
    return readStatusService.update(readStatusId, readStatusUpdateRequest);
  }

  @Operation(summary = "사용자의 메세지 읽음 상태 조회")
  @GetMapping
  @Override
  public List<ReadStatusDto> findByUserId(
      @RequestParam UUID userId
  ) {
    return readStatusService.findAllByUserId(userId);
  }
}
