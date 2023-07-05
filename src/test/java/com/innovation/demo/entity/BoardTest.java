package com.innovation.demo.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class BoardTest {
    @Test
    @DisplayName("board 객체 생성 테스트")
    void createBoardEntityTest() {
        String title = "testTitle";
        String content = "TestContent";
        Board board = new Board(title, content);

        assertThat(board).isNotNull();
        assertThat(board.getTitle()).isEqualTo(title);
        assertThat(board.getContent()).isEqualTo(content);
    }
}