package com.innovation.demo.service;

import com.innovation.demo.dto.LoginRequestDto;
import com.innovation.demo.repository.UserRepository;
import com.innovation.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import com.innovation.demo.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${token.name.LoginToken}")
    private String headerName;

    public void signup(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = passwordEncoder.encode(loginRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }

        User user = new User(username, password);
        userRepository.save(user);
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 회원 가입여부 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        Map<String,String> claims = new HashMap<>();

        claims.put("username", user.getUsername());

        // JWT 생성 후 쿠키로 res에 추가
        String token = jwtUtil.createToken(claims);
        jwtUtil.addJwtToCookie(headerName, token, res);
    }


}