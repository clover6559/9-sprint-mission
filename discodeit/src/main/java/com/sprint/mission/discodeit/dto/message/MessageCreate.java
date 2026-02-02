package com.sprint.mission.discodeit.dto.message;

import java.util.List;
import java.util.UUID;

public record MessageCreate(BasicMessageInfo basicMessageInfo, BinaryCreateDto binaryCreateDto) {
    public record BasicMessageInfo(UUID channelId, UUID senderId, String content, List<BinaryCreateDto> attachments){}
    public record BinaryCreateDto(String fileName, byte[] data) {}
}
