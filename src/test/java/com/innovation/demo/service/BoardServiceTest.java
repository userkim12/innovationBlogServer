package com.innovation.demo.service;

import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.entity.User;
import com.innovation.demo.repository.BoardRepository;
import com.innovation.demo.repository.UserRepository;
import com.innovation.demo.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockCookie;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardService boardService;

    @Autowired
    JwtUtil jwtUtil;

    private String baseName = "InitTitle";

    @BeforeEach
    void init() {
        Board board = new Board(baseName, "InitPassword");
        User user = new User("TestName", "TestPassword");
        userRepository.save(user);

        board.setUser(user);
        user.addBoard(boardRepository.save(board));
    }
//    @BeforeEach
//    void init() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(0);
//        Board board = new Board(baseName, "InitPassword");
//        User user = new User("TestName", "TestPassword");
//        userRepository.save(user);
//        board.setUser(user);
//        initBoard = boardRepository.save(board);
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
//    }

    @Test
    // Rollback 되는지
    @DisplayName("게시글 등록 테스트")
    void createBoardTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        Map<String, String> claims = new HashMap<>();
        claims.put("username", "TestName");
        String token = jwtUtil.createToken(claims);
        MockCookie cookie = new MockCookie("LoginToken", token);
        servletRequest.setCookies(cookie);

        BoardPostDto postDto = new BoardPostDto();
        postDto.setTitle("TestTitle");
        postDto.setContent("TestContent");

//        Board createdBoard = boardService.createBoard(postDto, servletRequest);
//        assertThat(createdBoard.getTitle()).isEqualTo(postDto.getTitle());
//        assertThat(createdBoard.getContent()).isEqualTo(postDto.getContent());
//        assertThat(boardService.findAllBoards().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("한 게시글 찾기 테스트")
    void findBoardTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        Board initBoard = boardService.findAllBoards().get(0);
        assertThat(boardService.findBoard(initBoard.getBoardId()).getTitle()).isEqualTo(baseName);
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
    }

    @Test
//    @Transactional // 얘는 왜..
    @DisplayName("게시글 단건 조회 실패 테스트")
    void findWrongIdBoardTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        assertThatThrownBy(() -> boardService.findBoard(2165789L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("데이터가 존재하지 않습니다.");
    }

    @Test
    @DisplayName("여러 게시글 찾기 테스트")
    void findAllBoardsTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        assertThat(boardService.findAllBoards().get(0).getTitle()).isEqualTo(baseName);
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updateBoardTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        Board initBoard = boardService.findAllBoards().get(0);
        long boardId = initBoard.getBoardId();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        Map<String, String> claims = new HashMap<>();
        claims.put("username", "TestName");
        String token = jwtUtil.createToken(claims);
        MockCookie cookie = new MockCookie("LoginToken", token);
        servletRequest.setCookies(cookie);

        BoardPatchDto patchDto = new BoardPatchDto();
        patchDto.setTitle("UpdateName");
        patchDto.setContent("UpdateContent");

//        Board findBoard = boardService.updateBoard(boardId, patchDto, servletRequest);
//        assertThat(findBoard.getTitle()).isEqualTo("UpdateName");
//        assertThat(findBoard.getContent()).isEqualTo("UpdateContent");
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteBoardTest() {
//        assertThat(boardService.findAllBoards().size()).isEqualTo(1);
        Board initBoard = boardService.findAllBoards().get(0);
        long boardId = initBoard.getBoardId();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        Map<String, String> claims = new HashMap<>();
        claims.put("username", "TestName");
        String token = jwtUtil.createToken(claims);
        MockCookie cookie = new MockCookie("LoginToken", token);
        servletRequest.setCookies(cookie);

//        boardService.deleteBoard(boardId, servletRequest);
//
//        assertThat(boardService.findAllBoards().size()).isEqualTo(0);
    }

}


