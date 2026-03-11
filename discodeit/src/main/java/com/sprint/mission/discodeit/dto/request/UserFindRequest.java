package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserFindRequest(boolean userStatus, UUID userId, String userName, String email) {

}
