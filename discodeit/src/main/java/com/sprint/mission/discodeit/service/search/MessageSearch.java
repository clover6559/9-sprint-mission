package com.sprint.mission.discodeit.service.search;

public class MessageSearch {
    private String userName;
    private String channelName;


    public MessageSearch(String userName, String channelName) {
        this.userName = userName;
        this.channelName = channelName;
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelName() {
        return channelName;
    }

}