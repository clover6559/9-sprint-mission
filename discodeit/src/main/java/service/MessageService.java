package service;

import entity.Channel;
import entity.Message;
import entity.User;
import service.serch.MessageSearch;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    //생성
    Message create(String content, User user, Channel channel);

    //조회(Id)
    Message findById(UUID messageId);

    //다건 조회 & 전체조회
    List<Message> Search(MessageSearch condition);
    // 전체조회
    List<Message> findAll();
    //수정
    Message update(UUID messageId, String content);

    //삭제
    boolean delete(UUID messageId);
}