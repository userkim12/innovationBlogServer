package com.innovation.demo.service;

import com.innovation.demo.dto.LoginRequestDto;
import com.innovation.demo.entity.User;
import com.innovation.demo.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.SoftAssertions;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User user;
    String baseName = "TestName";

    @BeforeEach
    void init() {
        User user = new User(baseName, passwordEncoder.encode("TestPassword"));
        userRepository.save(user);
        user = userRepository.findByUsername("TestName").get();
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void signUpTest() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                        .username("SuccessName")
                        .password("SuccessPassword")
                        .build();

        userService.signup(loginRequestDto);

        User findUser = userRepository.findByUsername("SuccessName").get();
        assertThat(findUser).isNotNull();
        assertThat(findUser.getUsername()).isEqualTo("SuccessName");
    }

    @Test
    @DisplayName("중복된 회원가입 시도 테스트")
    void signUpWithExistNameTest() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username(baseName)
                .password("TestPassword")
                .build();


        assertThatThrownBy(() -> userService.signup(loginRequestDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 사용자입니다.");
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void loginSuccessTest() {
        LoginRequestDto loginRequestDto = LoginRequestDto.builder()
                .username(baseName)
                .password("TestPassword")
                .build();

        MockHttpServletResponse servletResponse = new MockHttpServletResponse();
        userService.login(loginRequestDto, servletResponse);

        Cookie[] cookies = servletResponse.getCookies();

        assertThat(cookies.length).isEqualTo(1);
        assertThat(cookies[0].getName()).isEqualTo("LoginToken");
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFailureTest() {
        LoginRequestDto wrongPasswordDto = LoginRequestDto.builder()
                .username(baseName)
                .password("estPassword")
                .build();
        LoginRequestDto wrongIdDto = LoginRequestDto.builder()
                .username("idididid")
                .password("TestPassword")
                .build();

        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        assertThatThrownBy(() -> userService.login(wrongPasswordDto, servletResponse))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");

        assertThatThrownBy(() -> userService.login(wrongIdDto, servletResponse))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }
}