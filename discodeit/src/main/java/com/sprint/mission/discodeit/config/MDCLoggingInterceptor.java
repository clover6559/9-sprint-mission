package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class MDCLoggingInterceptor implements HandlerInterceptor {

    private static final String REQUEST_ID_HEADER = "Discodeit-Request-ID";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 랜덤한 요청 ID 생성 (UUID)
        String requestId = UUID.randomUUID().toString().substring(0, 8); // 8자리만 써도 충분해!

        // 2. MDC에 정보 추가 (Logback 패턴에서 쓸 이름들)
        MDC.put("request_id", requestId);
        MDC.put("method", request.getMethod());
        MDC.put("url", request.getRequestURI());

        // 3. 응답 헤더에 요청 ID 포함
        response.setHeader(REQUEST_ID_HEADER, requestId);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        MDC.clear();
    }
}
