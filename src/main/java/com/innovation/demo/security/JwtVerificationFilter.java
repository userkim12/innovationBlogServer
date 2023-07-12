package com.innovation.demo.security;


import com.innovation.demo.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtVerificationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl serviceImpl;

    public JwtVerificationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl serviceImpl) {
        this.jwtUtil = jwtUtil;
        this.serviceImpl = serviceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getTokenFromRequest(request);
        if (StringUtils.hasText(token)) {
            token = jwtUtil.substringToken(token);
            if (!jwtUtil.validateToken(token)) {
                System.out.println("뭐하노;;");
                return;
            }
            System.out.println(token);
            Claims claims = jwtUtil.getUserInfoFromToken(token);
            System.out.println("JwtVerificationFilter에서 JWT속 username 확인: " + claims.get("username"));
            try {
                setAuthentication(claims.get("username", String.class));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication auth = createAuthentication(username);
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = serviceImpl.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
