package repository;


import entity.Message;
import service.serch.MessageSearch;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    Message save(Message message);

    //조회(Id)
    Message findById(UUID massageId);

    //다건 조회 & 전체조회
    List<Message> MessageSearch(MessageSearch condition);
    // 전체조회
    List<Message> findAll();
    //수정
    Message updateMessage(UUID massageId, String content);

    //삭제
    boolean deleteById(UUID massageId);
}
