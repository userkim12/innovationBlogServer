package com.innovation.demo.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j(topic = "Token Util")
public class JwtUtil implements InitializingBean {

    // Token Sign
    public static final String BEARER_PREFIX = "Bearer ";

    // Token Algorithm
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    // Token MaxTime
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }


    // 1. CreateToken (sub)
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 1 - 1. CreateToken (claims)
    public String createToken(Map<String, String> body) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setClaims(body) // 사용자 식별자값(ID)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // 2. JWT Package for Cookie
    public void addJwtToCookie(String Header, String token,  HttpServletResponse res) {
        try {
            token = URLEncoder.encode(token, "utf-8").replaceAll("\\+", "%20"); // Cookie Value 에는 공백이 불가능해서 encoding 진행

            Cookie cookie = new Cookie(Header, token); // Name-Value
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");

            // Response 객체에 Cookie 추가
            res.addCookie(cookie);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
    }

    // 3. Token Validate
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 4. Get username for Cookie
    public String getUsernameFromCookie(Cookie cookie){
        String Token = substringToken(DecodeCookie(cookie));
        if(validateToken(Token)){
            Claims body = getUserInfoFromToken(Token);
            return body.get("username",String.class);
        }
        log.error("Token Error");
        return null;
    }

    // 4-1. Get username for Request
    public String getUsernameFromRequest(String TokenName, HttpServletRequest request){
        String Token = substringToken(getTokenFromRequest(TokenName, request));
        log.error(Token);
        if(validateToken(Token)){
            Claims body = getUserInfoFromToken(Token);
            log.error(body.get("username", String.class));
            return body.get("username", String.class);
        }
        log.error("Token Error");
        return null;
    }

    // 5. HttpServletRequest for Get Cookie (Name)
    public String getTokenFromRequest(String TokenName, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(TokenName)) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
                    } catch (UnsupportedEncodingException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    // Sub Fuction. Decode Cookie
    public String DecodeCookie(Cookie cookie) {
        try {
            return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    // Sub Function. Bearer substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        log.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
