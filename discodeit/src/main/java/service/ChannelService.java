package service;

import entity.Channel;
import entity.User;
import service.serch.ChannelSearch;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    //생성
    Channel createChannel(Channel.ChannelType channelType, String channelName, String description, User user);
    //조회(Id)
    Channel findChannelById(UUID channelId);
    //다건 조회
    List<Channel> ChannelSearch(ChannelSearch channelSearch);
    // 전체조회
    List<Channel> findAllChannel();
    //수정
    Channel updateChannel(UUID channelId, String channelName, String description);
    //삭제
    boolean deleteChannel(UUID channelId);
}
