package service.serch;

public class MessageSearch {
    private String userName;
    private String channelName;


    public MessageSearch(String userName, String channelName) {
        this.userName = userName;
        this.channelName = channelName;
    }
    public static MessageSearch byUserName(String userName) {
        return new MessageSearch(userName, null);
    }
    public MessageSearch byChannelName(String userName) {
        return new MessageSearch(null, channelName);    }

    public static MessageSearch byAll(String userName, String channelName) {
        return new MessageSearch(userName, channelName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

}