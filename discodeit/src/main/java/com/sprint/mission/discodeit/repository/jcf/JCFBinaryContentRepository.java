package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
@Repository
public class JCFBinaryContentRepository implements BinaryContentRepository {
    private final Map<UUID, BinaryContent>  binaryContentMap = new HashMap<>();
    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        binaryContentMap.put(binaryContent.getId(),binaryContent);
        return binaryContent;
    }

    @Override
    public BinaryContent findByRefId(UUID refId) {
        return binaryContentMap.values().stream()
                .filter(content -> content.getRefId().equals(refId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(UUID id) {
        binaryContentMap.remove(id);

    }
}
