package com.joker.apostas.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {

    private static final String TRACE_ID = "traceId";

    @Override
    public boolean preHandle(
            HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. Generate a short, unique ID
        String traceId = UUID.randomUUID().toString().substring(0, 8);

        // 2. Put it in the MDC context
        MDC.put(TRACE_ID, traceId);

        request.setAttribute("startTime", System.currentTimeMillis());

        String user =
                (request.getUserPrincipal() != null)
                        ? request.getUserPrincipal().getName()
                        : "Anonymous";
        log.info(
                ">>> INBOUND | Method: {} | Path: {} | User: {}",
                request.getMethod(),
                request.getRequestURI(),
                user);

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        try {
            Long startTime = (Long) request.getAttribute("startTime");
            long duration = (startTime != null) ? System.currentTimeMillis() - startTime : 0;

            log.info("<<< OUTBOUND | Status: {} | Duration: {}ms", response.getStatus(), duration);
        } finally {
            // 3. MUST CLEAR MDC!
            // Threads are reused in Tomcat; if you don't clear, the next request
            // might "inherit" the old traceId.
            MDC.clear();
        }
    }
}
