package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.serch.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //생성
    Channel create(Channel.ChannelType channelType, String channelName, String description, User user);

    //조회(Id)
    Channel findCById(UUID channelId);

    //다건 조회
    List<Channel> Search(ChannelSearch channelSearch);

    // 전체조회
    List<Channel> findAll();

    //수정
    String update(UUID channelId, String channelName, String description);

    //삭제
    boolean delete(UUID channelId);

    void printRemainChannel();

}
