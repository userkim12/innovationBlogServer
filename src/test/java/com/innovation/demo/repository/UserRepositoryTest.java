package com.innovation.demo.repository;

import com.innovation.demo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 이름으로 엔티티 조회 테스트")
    void findByUsernameTest() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User(username, password);

        userRepository.save(user);
        User findUser = userRepository.findByUsername(username).get();

        assertThat(findUser.getUsername()).isEqualTo(username);
        assertThat(findUser.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("유저 이름으로 엔티티 조회 실패 테스트")
    void findByWrongUsernameTest() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User(username, password);

        userRepository.save(user);
        User findUser = userRepository.findByUsername("wrongUsername").orElse(null);

        assertThat(findUser).isNull();
    }

}