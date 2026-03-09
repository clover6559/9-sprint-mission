package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.request.MessageCreate;
import com.sprint.mission.discodeit.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @PostMapping(
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<MessageDto> create(
      @RequestPart("messageCreateRequest") MessageCreate messageCreate,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
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
    MessageDto createdMessage = messageService.create(messageCreate, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }

  @PatchMapping("/{messageId}")
  @Override
  public ResponseEntity<Void> update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdate messageUpdate
  ) {
    messageService.update(messageId, messageUpdate);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{messageId}")
  @Override
  public ResponseEntity<Void> delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.noContent().build();
  }

  @GetMapping
  @Override
  public ResponseEntity<List<MessageDto>> findByChannelId(
      @RequestParam UUID channelId
  ) {
    List<MessageDto> messageList = messageService.findAllByChannelId(channelId);
    return ResponseEntity.ok(messageList);
  }

  @GetMapping("/pages")
  public PageResponse<MessageDto> pages(
      @RequestParam String content,
      @RequestParam Channel channel,
      @RequestParam User author,
      @PageableDefault(size = 50, sort = "createdAt", direction = Direction.DESC) Pageable pageable
  ) {
    return messageService.findSliceByContent(content, channel, author, pageable);
  }
}



