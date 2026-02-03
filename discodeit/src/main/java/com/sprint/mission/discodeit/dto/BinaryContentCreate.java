package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record BinaryContentCreate(UUID refId, String fileName, byte[] data) {
}
