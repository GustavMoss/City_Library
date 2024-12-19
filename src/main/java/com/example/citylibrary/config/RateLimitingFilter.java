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

    //Håller reda på antal requests & tidstämpel för varje IP-adress
    private static class RequestInfo {
        private int count;
        private Instant timestamp;

        RequestInfo(int count, Instant timestamp) {
            this.count = count;
            this.timestamp = timestamp;
        }
    }

    //Lagrar request-information för varje IP-adress
    private final Map<String, RequestInfo> requestCounts = new ConcurrentHashMap<>();

    private static final int MAX_REQUESTS_PER_MINUTE = 10;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //Hämta klientens IP-adress
        String clientIp = request.getRemoteAddr();
        Instant now = Instant.now();

        //Mappa ny post om IP-adressen inte redan finns
        requestCounts.putIfAbsent(clientIp, new RequestInfo(0, now));
        RequestInfo requestInfo = requestCounts.get(clientIp);

        //Återställ antal requests om det har gått mer än en minut sedan senaste förfrågan
        if (now.isAfter(requestInfo.timestamp.plusSeconds(60))) {
            requestInfo.count = 0;
            requestInfo.timestamp = now;
        }

        //Returnera status 429 om antal requests överskrider maxgränsen
        if (requestInfo.count >= MAX_REQUESTS_PER_MINUTE) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests - please try again in a minute.");
            return;
        }

        //Öka antal requests & fortsätt till nästa filter i kedjan
        requestInfo.count++;
        filterChain.doFilter(request, response);
    }
}
