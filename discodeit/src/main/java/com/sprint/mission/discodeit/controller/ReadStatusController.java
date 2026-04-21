package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.service.ReadStatusService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/readStatuses")
@RestController
@RequiredArgsConstructor
public class ReadStatusController implements ReadStatusApi {

    private final ReadStatusService readStatusService;

    @Operation(summary = "읽음 상태 생성")
    @PostMapping
    @Override
    public ResponseEntity<ReadStatusDto> create(@Valid @RequestBody ReadStatusCreateRequest readStatusCreateRequest) {
        ReadStatusDto createdReadStatus = readStatusService.create(readStatusCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReadStatus);
    }

    @Operation(summary = "읽음 상태 수정")
    @PatchMapping("/{readStatusId}")
    @Override
    public ResponseEntity<ReadStatusDto> update(
            @PathVariable UUID readStatusId, @Valid @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest) {
        ReadStatusDto updatedReadStatus = readStatusService.update(readStatusId, readStatusUpdateRequest);
        return ResponseEntity.status(HttpStatus.OK).body(updatedReadStatus);
    }

    @Operation(summary = "사용자의 메세지 읽음 상태 조회")
    @GetMapping
    @Override
    public ResponseEntity<List<ReadStatusDto>> findByUserId(@RequestParam UUID userId) {
        List<ReadStatusDto> readStatuses = readStatusService.findAllByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(readStatuses);
    }
}
