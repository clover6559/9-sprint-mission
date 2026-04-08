package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

    private final MessageService messageService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Override
    public ResponseEntity<MessageDto> create(
            @Valid @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
        log.info(
                "메세지 생성 요청 수신 - 채널 ID: {}, 유저 ID: {}, 내용: {}",
                messageCreateRequest.channelId(),
                messageCreateRequest.authorId(),
                messageCreateRequest.content());
        List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
                .map(files -> files.stream()
                        .map(file -> {
                            try {
                                log.info(
                                        "첨부파일 등록 요청 - 파일 이름 : {}, 파일 용량: {} bytes",
                                        file.getOriginalFilename(),
                                        file.getSize());
                                return new BinaryContentCreateRequest(
                                        file.getOriginalFilename(), file.getContentType(), file.getBytes());
                            } catch (IOException e) {
                                log.error("파일 데이터 읽기 실패 - 파일 이름: {}", file.getOriginalFilename(), e);
                                throw new RuntimeException("파일 처리 중 오류 발생", e);
                            }
                        })
                        .toList())
                .orElse(new ArrayList<>());
        log.info("첨부파일 변환 요청 처리 완료 - 총 {}개", attachmentRequests.size());
        MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
        log.info("메세지 생성 요청 처리 완료 - 메세지 ID: {}", createdMessage.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
    }

    @PatchMapping("/{messageId}")
    @Override
    public ResponseEntity<MessageDto> update(
            @PathVariable UUID messageId, @Valid @RequestBody MessageUpdateRequest messageUpdateRequest) {
        log.info("메세지 업데이트 요청 수신 - 메세지 ID: {}, 변경할 내용: {}", messageId, messageUpdateRequest.newContent());
        MessageDto updatedMessage = messageService.update(messageId, messageUpdateRequest);
        log.info("메세지 업데이트 요청 처리 완료 - 메세지 ID: {}", messageId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
    }

    @DeleteMapping("/{messageId}")
    @Override
    public ResponseEntity<Void> delete(@PathVariable UUID messageId) {
        log.info("메세지 삭제 요청 수신 - 메세지 ID: {}", messageId);
        messageService.delete(messageId);
        log.info("메세지 삭제 요청 처리 완료 - 메세지 ID: {}", messageId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    @Override
    public ResponseEntity<PageResponse<MessageDto>> findByChannelId(
            @RequestParam UUID channelId,
            @RequestParam(required = false) Instant cursor,
            @PageableDefault(size = 50, page = 0, sort = "createdAt", direction = Direction.DESC) Pageable pageable) {
        PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(messages);
    }
}
