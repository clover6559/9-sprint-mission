package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    private final BasicMessageService messageService;
    @MessageMapping("/messages")
    @SendTo("/sub/messages")
    public MessageDto sendMessage(@Payload MessageCreateRequest request) {
        return messageService.create(request, null);
    }
}
