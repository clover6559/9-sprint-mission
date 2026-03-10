package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
  public MessageDto create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
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
    return messageService.create(messageCreateRequest, attachmentRequests);
  }

  @PatchMapping("/{messageId}")
  @Override
  public MessageDto update(
      @PathVariable UUID messageId,
      @RequestBody MessageUpdateRequest messageUpdateRequest
  ) {
    return messageService.update(messageId, messageUpdateRequest);
  }

  @DeleteMapping("/{messageId}")
  @Override
  public void delete(
      @PathVariable UUID messageId
  ) {
    messageService.delete(messageId);
  }


  @GetMapping
  @Override
  public PageResponse<MessageDto> findByChannelId(
      @RequestParam UUID channelId,
      @RequestParam(required = false) Instant cursor,
      @ParameterObject Pageable pageable
  ) {
    return messageService.getMessages(channelId, cursor, pageable);
  }

}



