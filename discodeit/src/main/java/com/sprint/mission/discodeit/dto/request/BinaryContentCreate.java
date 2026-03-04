package com.sprint.mission.discodeit.dto.request;


public record BinaryContentCreate(String fileName,
                                  String contentType,
                                  byte[] bytes
) {

}
