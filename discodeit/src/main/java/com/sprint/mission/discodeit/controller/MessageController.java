package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Tag(name = "Message API")
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @Operation(summary = "메세지 생성")
  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreate messageCreate,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) throws IOException {
    List<BinaryContentCreate> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreate(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    Message createdMessage = messageService.create(messageCreate, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @Operation(summary = "메세지 내용 수정")
  @PatchMapping("/{messageId}")
  public ResponseEntity<Void> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdate messageUpdate
  ) {
    messageService.update(messageId, messageUpdate);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "메세지 삭제")
  @DeleteMapping("/{messageId}")
  public ResponseEntity<Void> delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "채널 메세지 조회")
  @GetMapping
  public ResponseEntity<List<Message>> findByChannelId(
      @RequestParam UUID channelId
  ) {
    List<Message> messageList = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messageList);
  }
}



