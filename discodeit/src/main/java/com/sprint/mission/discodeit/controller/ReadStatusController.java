package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "ReadStatus API")
@RequestMapping("/api/readStatuses")
@RestController
@RequiredArgsConstructor
public class ReadStatusController {

  private final ReadStatusService readStatusService;

  @Operation(summary = "읽음 상태 생성")
  @PostMapping
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreate readStatusCreate
  ) {
    ReadStatus readStatus = readStatusService.create(readStatusCreate);
    return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
  }

  @Operation(summary = "읽음 상태 수정")
  @PatchMapping("/{readStatusId}")
  public ResponseEntity<ReadStatus> update(
      @PathVariable UUID readStatusId,
      @RequestBody ReadStatusUpdate readStatusUpdate
  ) {
    ReadStatus readStatus = readStatusService.update(readStatusId, readStatusUpdate);
    return ResponseEntity.ok(readStatus);
  }

  @Operation(summary = "사용자의 메세지 읽음 상태 조회")
  @GetMapping
  public ResponseEntity<List<ReadStatus>> findByUserId(
      @RequestParam UUID userId
  ) {
    List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
    return ResponseEntity.ok(readStatusList);
  }
}
