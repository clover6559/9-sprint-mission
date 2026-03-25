package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public BinaryContentDto create(BinaryContentCreateRequest create) {
    String fileName = create.fileName();
    byte[] bytes = create.bytes();
    String contentType = create.contentType();
    log.info("첨부파일 생성 요청 - 파일 이름: {}, 파일 타입: {}, 파일 용량: {} bytes", fileName, contentType,
        bytes.length);
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    BinaryContent savedbinaryContent = binaryContentRepository.save(binaryContent);
    log.info("첨부파일 생성 성공 - 파일 ID: {}, 파일 이름: {}", savedbinaryContent.getId(), fileName);
    binaryContentStorage.put(savedbinaryContent.getId(), bytes);
    return binaryContentMapper.toDto(savedbinaryContent);
  }

  @Override
  public BinaryContentDto find(UUID binaryContentId) {
    log.info("첨부파일 조회 - 파일 ID: {}", binaryContentId);
    return binaryContentRepository.findById(binaryContentId)
        .map(binaryContentMapper::toDto)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 첨부파일로 조회 실패 - 파일 ID: {}", binaryContentId);
          return new RuntimeException("해당 바이너리 데이터를 찾을 수 없습니다.");
        });
  }

  @Override
  public List<BinaryContentDto> findAllByIdIn(List<UUID> binaryContentIds) {
    log.debug("조회할 파일 ID 목록: {}", binaryContentIds);
    return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
        .map(binaryContentMapper::toDto).toList();
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    log.info("첨부파일 삭제 요청 - 첨부파일 ID: {}", binaryContentId);
    BinaryContent foundContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 첨부파일로 삭제 실패 - 첨부파일 ID: {}", binaryContentId);
          return new RuntimeException("해당 첨부파일을 찾을 수 없습니다.");
        });
    binaryContentRepository.delete(foundContent);
    log.info("첨부파일 삭제 성공 - 첨부파일 ID: {}", binaryContentId);

  }
}
