package service.serch;

public class ChannelSearch {
    private String userName;
    private String channelName;

    public ChannelSearch(String userName, String channelName) {
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


