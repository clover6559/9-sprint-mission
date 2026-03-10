package com.sprint.mission.discodeit.api.controller;

import com.sprint.mission.discodeit.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping
  @Override
  public List<BinaryContentDto> findAllByIdIn(
      @RequestParam("binaryContentIds") List<UUID> binaryContentIds
  ) {
    return binaryContentService.findAllByIdIn(binaryContentIds);
  }

  @GetMapping("/{binaryContentId}")
  @Override
  public BinaryContentDto find(
      @PathVariable UUID binaryContentId
  ) throws RuntimeException {
    return binaryContentService.find(binaryContentId);
  }

  @GetMapping("/{binaryContentId}/download")
  @Override
  public ResponseEntity<?> download(
      @PathVariable UUID binaryContentId
  ) throws RuntimeException {
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    return binaryContentStorage.download(binaryContent);
  }

}
