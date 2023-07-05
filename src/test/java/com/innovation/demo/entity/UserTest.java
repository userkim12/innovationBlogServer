package com.innovation.demo.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("유저 객체 생성 테스트")
    void createUserEntityTest() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User(username, password);

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getBoardList()).isEmpty();
    }

    @Test
    @DisplayName("유저 엔티티 게시글 추가 테스트")
    void addBoardTest() {
        String username = "testUsername";
        String password = "testPassword";
        User user = new User(username, password);

        String title = "testTitle";
        String content = "TestContent";
        Board board = new Board(title, content);

        user.addBoard(board);

        assertThat(user.getBoardList().size()).isEqualTo(1);
        assertThat(user.getBoardList().contains(board)).isTrue();
    }
}