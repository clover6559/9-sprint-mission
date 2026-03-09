package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice, Function<T, Object> cursorExtractor,
      Long totalElements) {
    Object nextCursor = null;
    if (slice.hasNext() && !slice.getContent().isEmpty()) {
      T lastItem = slice.getContent().get(slice.getContent().size() - 1);
      nextCursor = cursorExtractor.apply(lastItem);
    }

    return new PageResponse<>(slice.getContent(), nextCursor, slice.getSize(), slice.hasNext(),
        slice.stream().count());
  }
}
