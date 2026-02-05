package com.sprint.mission.discodeit.dto.message;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public record MessageCreate(BasicMessageInfo basicMessageInfo, BinaryCreateDto binaryCreateDto) {
    public record BasicMessageInfo(UUID channelId, UUID senderId, String content, List<BinaryCreateDto> attachments){}
    public record BinaryCreateDto(String fileName, byte[] data) {}

}
