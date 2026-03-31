package com.sprint.mission.discodeit.exception.BinaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.UUID;

public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(UUID binaryContentId) {
        super(ErrorCode.BINARYCONTENT_NOT_FOUND, String.format("파일을 찾을 수 없습니다: ID=%s", binaryContentId));
        addDetails("binaryContentId", binaryContentId);
        addDetails("searchType", "byId");
    }
}
