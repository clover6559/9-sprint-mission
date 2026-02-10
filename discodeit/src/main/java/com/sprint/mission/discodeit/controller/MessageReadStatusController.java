package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusCreate;
import com.sprint.mission.discodeit.dto.ReadStatus.ReadStatusUpdate;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/readstatus")
@RestController
@RequiredArgsConstructor
public class MessageReadStatusController {
    private final ReadStatusService readStatusService;

    //특정 채널의 메세지 수신정보 생성, 수신정보 수정, 특정 사용자의 수신 정보 조회
    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST
    )
    public ResponseEntity<ReadStatus> create(
            @RequestBody ReadStatusCreate readStatusCreate
            ){
        ReadStatus readStatus = readStatusService.create(readStatusCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(readStatus);
    }

    @RequestMapping(
            path = "/readStatusId}}",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<ReadStatus> update(
            @PathVariable UUID readStatusId,
            @RequestBody ReadStatusUpdate readStatusUpdate
            ){
        ReadStatus readStatus = readStatusService.update(readStatusId,readStatusUpdate);
        return ResponseEntity.ok(readStatus);
    }

    @RequestMapping(
            path = "/find/{userId}",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<ReadStatus>> findByUserId(
            @PathVariable UUID userId
    ) {
        List<ReadStatus> readStatusList = readStatusService.findAllByUserId(userId);
        return ResponseEntity.ok(readStatusList);
    }
}
