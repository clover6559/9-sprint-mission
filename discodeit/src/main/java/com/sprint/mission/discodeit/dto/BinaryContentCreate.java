package com.sprint.mission.discodeit.dto;


public record BinaryContentCreate(String fileName,
                                  String contentType,
                                  byte[] bytes
) {

}
