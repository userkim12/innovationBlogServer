package com.innovation.demo.repository;

import com.innovation.demo.entity.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    @DisplayName("게시글 내림차순 조회 테스트")
    void findAllByCreatedAtDescTest() throws InterruptedException {
        boardRepository.save(new Board("testTitle01", "testContent01"));
        Thread.sleep(1000);
        boardRepository.save(new Board("testTitle02", "testContent02"));

        List<Board> findBoards = boardRepository.findAllByOrderByCreatedAtDesc();

        assertThat(findBoards.get(0).getTitle()).isEqualTo("testTitle02");
    }
}