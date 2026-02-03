package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {
    BinaryContent save(BinaryContent binaryContent);
    Optional<BinaryContent> findByRefId(UUID refId);
    Optional<BinaryContent> findById(UUID Id);
    boolean existsByRefId(UUID refId);
    List<BinaryContent> findAll();
    void deleteById(UUID id);
    void deleteByRefId(UUID refId);

}
