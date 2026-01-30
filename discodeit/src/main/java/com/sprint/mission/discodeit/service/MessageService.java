package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.search.MessageSearch;

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
    String update(UUID messageId, String content);

    //삭제
    boolean delete(UUID messageId);

    void printRemainMessages();

    interface ReadStatusService {
    }
}