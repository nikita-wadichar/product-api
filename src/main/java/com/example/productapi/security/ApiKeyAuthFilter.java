package com.example.productapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class ApiKeyAuthFilter extends OncePerRequestFilter {

    private final String apiKey;

    public ApiKeyAuthFilter(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/swagger-ui") ||
                path.startsWith("/api-docs") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars") ||
                path.startsWith("/swagger-resources") ||
                path.startsWith("/api/v1/auth") ||
                path.equals("/") ||
                path.equals("/favicon.ico") ||
                path.contains("swagger") ||
                path.contains("api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String requestApiKey = request.getHeader("X-API-Key");

        if (apiKey.equals(requestApiKey)) {
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken("api-user", null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing API key\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}