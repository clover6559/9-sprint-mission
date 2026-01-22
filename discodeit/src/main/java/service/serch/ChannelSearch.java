package service.serch;

public class ChannelSearch {
    private String userName;
    private String channelName;

    public ChannelSearch(String userName, String channelName) {
        this.userName = userName;
        this.channelName = channelName;
    }
    public ChannelSearch byUserName(String userName) {
        return new ChannelSearch(userName, null);
    }
    public ChannelSearch byChannelName(String channelName) {
        return new ChannelSearch(null, this.channelName);    }

    public static ChannelSearch byAll(String userName, String channelName) {
        return new ChannelSearch(userName, channelName);
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelName() {
        return channelName;
    }
}


