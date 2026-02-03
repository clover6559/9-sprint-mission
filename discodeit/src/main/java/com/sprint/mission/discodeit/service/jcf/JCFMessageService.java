//package com.sprint.mission.discodeit.service.jcf;
//
//import com.sprint.mission.discodeit.dto.message.MessageCreate;
//import com.sprint.mission.discodeit.dto.message.MessageUpdate;
//import com.sprint.mission.discodeit.entity.BinaryContent;
//import com.sprint.mission.discodeit.entity.Message;
//import com.sprint.mission.discodeit.repository.BinaryContentRepository;
//import com.sprint.mission.discodeit.repository.ChannelRepository;
//import com.sprint.mission.discodeit.repository.MessageRepository;
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.service.search.MessageSearch;
//import com.sprint.mission.discodeit.service.MessageService;
//
//import java.util.*;
//
//public class JCFMessageService implements MessageService {
//    private final MessageRepository messageRepository;
//    private final ChannelRepository channelRepository;
//    private final UserRepository userRepository;
//    private final BinaryContentRepository binaryContentRepository;
//
//    public JCFMessageService(MessageRepository messageRepository, ChannelRepository channelRepository,UserRepository userRepository, BinaryContentRepository binaryContentRepository) {
//        this.messageRepository = messageRepository;
//        this.channelRepository = channelRepository;
//        this.userRepository = userRepository;
//        this.binaryContentRepository = binaryContentRepository;
//    }
//
//    @Override
//    public Message create(MessageCreate MessageCreate) {
//        Message message = new Message(MessageCreate);
//        if (MessageCreate.basicMessageInfo().attachments() != null && !MessageCreate.basicMessageInfo().attachments().isEmpty()) {
//            List<UUID> savedIds = MessageCreate.basicMessageInfo().attachments().stream()
//                    .map(dto -> new BinaryContent(message.getMessageId(), dto.fileName(), dto.data()))
//                    .map(binaryContentRepository::save)
//                    .map(BinaryContent::getId)
//                    .toList();
//            message.updateAttachmentIds(savedIds);
//        }
//        return messageRepository.save(message);
//    }
//
//    @Override
//    public Message findById(UUID messageId) {
//        return messageRepository.findById(messageId)
//                .orElseThrow(() -> new RuntimeException("해당 메시지를 찾을 수 없습니다."));
//    }
//
//    @Override
//    public List<Message> Search(MessageSearch messageSearch) {
//        return messageRepository.findAll().stream()
//                .filter(m-> messageSearch.getUserName() == null || messageSearch.getUserName().equals(m.getUserName()))
//                .filter(m -> messageSearch.getChannelName() == null || messageSearch.getChannelName().equals(m.getChannelName()))
//                .toList();
//    }
//
//    @Override
//    public List<Message> findAllByChannelId(UUID channelId) {
//        return messageRepository.findByChannelId(channelId);
//    }
//
//
//    @Override
//    public String update(MessageUpdate MessageUpdate) {
//        Message foundMessage = messageRepository.findById(MessageUpdate.targetId())
//                .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
//         foundMessage.update(MessageUpdate.content());
//        messageRepository.save(foundMessage);
//        return foundMessage.getContent();
//    }
//
//    @Override
//    public boolean delete(UUID messageId) {
//        Message findMessage = messageRepository.findById(messageId)
//                .orElseThrow(() -> new RuntimeException("해당 메세지를 찾을 수 없습니다."));
//        binaryContentRepository.deleteByRefId(findMessage.getMessageId());
//        messageRepository.deleteById(messageId);
//        return true;
//         }
//
//    @Override
//    public void printRemainMessages() {
//        List<Message> messages = messageRepository.findAll();
//        System.out.println("현재 남은 메세지 수: " + messages.size());
//        messages.forEach(m -> System.out.println("- " + m.getContent()));
//    }
//
//}
//
