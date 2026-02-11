package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record BinaryContentCreate(String fileName,
                                  String contentType,
                                  byte[] bytes) {
}
