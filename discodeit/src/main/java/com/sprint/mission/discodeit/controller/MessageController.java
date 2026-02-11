package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.BinaryContentCreate;
import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @RequestMapping(
            path = "/create",
            method = RequestMethod.POST,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Message> create(
            @RequestPart ("messageCreate") MessageCreate messageCreate,
            @RequestPart (value = "file", required = false) MultipartFile file
            ) throws IOException {
        BinaryContentCreate binaryCreate = null;        if (file != null && !file.isEmpty()) {
            binaryCreate = new BinaryContentCreate(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
        }
        Message message = messageService.create(messageCreate, binaryCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
        }

    @RequestMapping(
            path = "/update",
            method = RequestMethod.PATCH
    )
    public ResponseEntity<Void> update(
            @RequestBody MessageUpdate messageUpdate
            ) {
            messageService.update(messageUpdate);
            return ResponseEntity.ok().build();
        }

    @RequestMapping(
            path = "/{messageId}",
            method = RequestMethod.DELETE
    )
    public ResponseEntity<Void> delete(
            @PathVariable UUID messageId
            ){
        messageService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "/{channelId}/message",
            method = RequestMethod.GET
    )
    public ResponseEntity<List<Message>> findByChannelId(
            @PathVariable UUID channelId
    ){
        List<Message> messageList = messageService.findAllByChannelId(channelId);
        return ResponseEntity.ok(messageList);
    }
    }



