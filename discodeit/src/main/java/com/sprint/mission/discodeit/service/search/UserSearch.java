package com.sprint.mission.discodeit.service.search;

import com.sprint.mission.discodeit.entity.User;

public class UserSearch {
    private String userName;

    public UserSearch(String userName) {
        this.userName = userName;
    }
    public boolean matches(User user) {
        if (userName != null && !user.getUserName().contains(userName)) {
            return false;
        }
        return true;
    }

    public String getUserName() {
        return userName;
    }

}
