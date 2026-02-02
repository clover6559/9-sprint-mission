package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreate;
import com.sprint.mission.discodeit.dto.message.MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.search.MessageSearch;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    //생성
    Message create(MessageCreate messageCreate);

    //조회(Id)
    Message findById(UUID messageId);

    //다건 조회 & 전체조회
    List<Message> Search(MessageSearch condition);

    // 전체조회
    List<Message> findAllByChannelId(UUID channelId);

    //수정
    String update(MessageUpdate messageUpdate);

    //삭제
    boolean delete(UUID messageId);

    void printRemainMessages();

    interface ReadStatusService {
    }
}