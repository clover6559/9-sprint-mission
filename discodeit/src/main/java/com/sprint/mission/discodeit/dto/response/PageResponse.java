package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import java.util.List;
import org.springframework.data.domain.*;


public record PageResponse<T>(List<T> content, int number, int size, boolean hasNext,
                              Long totalElements) {

}
