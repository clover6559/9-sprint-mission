package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.BinaryContentApi;
import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/binaryContents")
@RequiredArgsConstructor
public class BinaryContentController implements BinaryContentApi {

  private final BinaryContentService binaryContentService;
  private final BinaryContentStorage binaryContentStorage;

  @GetMapping
  @Override
  public ResponseEntity<List<BinaryContentDto>> findAllByIdIn(
      @Valid @RequestParam("binaryContentIds") List<UUID> binaryContentIds) {
    log.info("첨부파일 목록 조회 요청 수신 - 개수: {}", binaryContentIds.size());
    List<BinaryContentDto> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
    log.info("첨부파일 목록 조회 요청 처리 완료 - 조회된 개수: {}", binaryContents.size());
    return ResponseEntity.status(HttpStatus.OK).body(binaryContents);
  }

  @GetMapping("/{binaryContentId}")
  @Override
  public ResponseEntity<BinaryContentDto> find(@PathVariable UUID binaryContentId) {
    log.info("첨부파일 정보 조회 요청 수신 - 파일 ID: {}", binaryContentId);
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    return ResponseEntity.status(HttpStatus.OK).body(binaryContent);
  }

  @GetMapping("/{binaryContentId}/download")
  @Override
  public ResponseEntity<?> download(@PathVariable UUID binaryContentId) {
    log.info("첨부파일 다운로드 요청 수신 - 파일 ID: {}", binaryContentId);
    BinaryContentDto binaryContent = binaryContentService.find(binaryContentId);
    log.info("첨부파일 다운로드 시작 - 파일 이름: {}, 크기: {} bytes", binaryContent.fileName(),
        binaryContent.size());
    return binaryContentStorage.download(binaryContent);
  }
}
