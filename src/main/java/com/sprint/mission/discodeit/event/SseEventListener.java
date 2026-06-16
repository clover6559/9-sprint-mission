package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.data.NotificationDto;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SseEventListener {

    private final SseService sseService;

    // 알림 생성 → 해당 유저에게만 전송
    public void sendNotification(List<Notification> notifications) {
        notifications.forEach(notification ->
                sseService.send(
                        List.of(notification.getReceiverId()),
                        "notifications.created",
                        NotificationDto.form(notification)
                )
        );
    }

    // 채널 생성/수정/삭제 → 전체 broadcast
    @EventListener
    public void on(ChannelSseEvent event) {
        sseService.broadcast(event.eventName(), event.channelDto());
    }

    // 유저 생성/수정/삭제 → 전체 broadcast
    @EventListener
    public void on(UserSseEvent event) {
        sseService.broadcast(event.eventName(), event.userDto());
    }

    // BinaryContent 상태 변경 → 해당 유저에게만 전송
    @EventListener
    public void on(BinaryContentSseEvent event) {
        sseService.send(
                List.of(event.receiverId()),
                "binaryContents.updated",
                event.binaryContentDto()
        );
    }
}