package com.example.citylibrary.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static class RequestInfo {
        private int count;
        private Instant timestamp;

        RequestInfo(int count, Instant timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }

    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String clientIp = request.getRemoteAddr();
        Instant now = Instant.now();

        requestCounts.putIfAbsent(clientIp, new RequestInfo(0, now));
        RequestInfo requestInfo = requestCounts.get(clientIp);

        if (now.isAfter(requestInfo.timestamp.plusSeconds(60))) {
            requestInfo.count = 0;
            requestInfo.timestamp = now;
        }

        if (requestInfo.count >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - please try again in a minute.");
            return;
        }

        requestInfo.count++;
        filterChain.doFilter(request, response);
    }
}
