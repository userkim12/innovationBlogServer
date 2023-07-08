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

    @Value("${token.name.Authorization}")
    private String headerName;

    @Value("${admin.username}")
    private String adminName;

    public void signup(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = passwordEncoder.encode(loginRequestDto.getPassword());

        // 회원 중복 확인
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        User user = new User(username, password);
        if (user.getUsername().equals(adminName)) {
            user.changeAdminRole();
        }

        userRepository.save(user);
    }

    public void login(LoginRequestDto loginRequestDto, HttpServletResponse res) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 회원 가입여부 확인
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("회원을 찾을 수 없습니다.")
        );

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }
        Map<String, String> claims = new HashMap<>();

        claims.put("username", user.getUsername());

        String token = jwtUtil.createToken(claims);
        res.addHeader(headerName, token);
    }


}