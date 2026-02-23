package com.sprint.mission.discodeit.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.stream()
        .filter(c -> c instanceof MappingJackson2HttpMessageConverter)
        .map(c -> (MappingJackson2HttpMessageConverter) c)
        .findFirst()
        .ifPresent(converter -> {
          // 기존에 지원하는 타입들을 가져와서
          List<MediaType> supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());

          // ⭐️ 핵심: 정체불명의 파일(octet-stream)도 JSON으로 해석하라고 추가!
          supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);

          // 다시 변환기에 적용
          converter.setSupportedMediaTypes(supportedMediaTypes);
        });
  }
}