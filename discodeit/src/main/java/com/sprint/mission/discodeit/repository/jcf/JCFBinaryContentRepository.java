package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;

import java.util.*;

public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent>  binaryContentMap = new HashMap<>();

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(),binaryContent);
        return binaryContent;
    }

    @Override
    public Optional<BinaryContent> findByRefId(UUID refId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.getRefId().equals(refId))
                .findFirst();
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.ofNullable(binaryContentMap.get(id));
    }

    @Override
    public boolean existsByRefId(UUID refId) {
        return binaryContentMap.values().stream()
                .anyMatch(content -> content.getRefId().equals(refId));
    }

    @Override
    public List<BinaryContent> findAll() {
        return binaryContentMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentMap.remove(id);
    }

    @Override
    public void deleteByRefId(UUID refId) {
        binaryContentMap.values().removeIf(content -> content.getRefId().equals(refId));
    }
    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        return binaryContentMap.values().stream()
                .filter(content -> ids.contains(content.getId()))
                .toList();
    }
}
