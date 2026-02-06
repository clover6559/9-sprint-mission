package com.sprint.mission.discodeit.service.search;

import lombok.Getter;

@Getter
public class MessageSearch {
    private String userName;
    private String channelName;

    public MessageSearch(String userName, String channelName) {
        this.userName = userName;
        this.channelName = channelName;
    }
}