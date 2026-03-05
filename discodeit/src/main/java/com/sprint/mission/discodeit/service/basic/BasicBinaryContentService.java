package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreate;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public BinaryContent create(BinaryContentCreate create) {
    String fileName = create.fileName();
    byte[] bytes = create.bytes();
    String contentType = create.contentType();
    BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length, contentType);
    return binaryContentRepository.save(binaryContent);
  }

  @Override
  public BinaryContent find(UUID binaryContentId) {
    return binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new RuntimeException("해당 바이너리 데이터를 찾을 수 없습니다."));
  }

  @Override
  public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
    return binaryContentRepository.findAllByIdIn(binaryContentIds);
  }

  @Transactional
  @Override
  public void delete(UUID binaryContentId) {
    BinaryContent foundContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> new RuntimeException("해당 바이너리 데이터를 찾을 수 없습니다."));
    binaryContentRepository.delete(foundContent);
  }
}
