package com.sprint.mission.discodeit.service.search;

import com.sprint.mission.discodeit.entity.Channel;
import lombok.Getter;
@Getter
public class ChannelSearch {
    private String userName;
    private String channelName;
    private Channel.ChannelType channelType;

    public ChannelSearch(String userName, String channelName, Channel.ChannelType channelType) {
        this.userName = userName;
        this.channelName = channelName;
        this.channelType = channelType;
    }
}


