//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.BinaryContentCreate;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.service.BinaryContentService;
//
//import java.util.List;
//import java.util.UUID;
//
//public class JCFBinaryContentService implements BinaryContentService {
//    private final BinaryContentRepository binaryContentRepository;
//
//    publicJCFBinaryContentService (BinaryContentRepository binaryContentRepository) {
//        this.binaryContentRepository = binaryContentRepository;
//    }
//
//    @Override
//    public BinaryContent create(BinaryContentCreate create) {
//        if (binaryContentRepository.existsByRefId(create.refId())) {
//            throw new RuntimeException("해당 참조 ID에 대한 바이너리 데이터가 이미 존재합니다.");
//        }
//        BinaryContent binaryContent = new BinaryContent(create.refId(), create.fileName(), create.data());
//        return binaryContentRepository.save(binaryContent);
//    }
//
//    @Override
//    public BinaryContent findById(UUID id) {
//        return binaryContentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 바이너리 데이터를 찾을 수 없습니다."));
//    }
//
//    @Override
//    public List<BinaryContent> findAllByIdIn(List<UUID> uuidList) {
//        return binaryContentRepository.findAll().stream()
//                .filter(content ->uuidList.contains(content.getId()))
//                .toList();
//    }
//
//    @Override
//    public boolean deleteById(UUID id) {
//        binaryContentRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("해당 바이너리 데이터를 찾을 수 없습니다."));
//        binaryContentRepository.deleteById(id);
//        return true;
//    }
//}