package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelResponse;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdate;
import com.sprint.mission.discodeit.dto.channel.CreatePublic;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.search.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //생성
    Channel create(CreatePublic createPublic);

    //조회(Id)
    ChannelResponse findCById(UUID channelId);

    //다건 조회
    List<Channel> Search(ChannelSearch channelSearch);

    // 전체조회
    List<ChannelResponse> findAllByUserId(UUID userId);

    //수정
    String update(ChannelUpdate update);

    //삭제
    boolean delete(UUID channelId);

    void printRemainChannel();
}
