package com.innovation.demo.mapper;

import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BoardMapperTest {

    BoardMapper boardMapper = Mappers.getMapper(BoardMapper.class);

    @Test
    @DisplayName("BoardPostDto to Board Test")
    void boardPostDtoToBoardTest() {
        BoardPostDto postDto = new BoardPostDto();
        postDto.setContent(null);
        postDto.setTitle("TestTitle");
        Board board = boardMapper.BoardPostDtoToBoard(postDto);

        assertThat(board.getTitle()).isEqualTo("TestTitle");
        assertThat(board.getContent()).isEqualTo(null);
    }

    @Test
    @DisplayName("BoardPatchDto to Board Test")
    void boardPatchDtoToBoardTest() {
        BoardPatchDto patchDto = new BoardPatchDto();
        patchDto.setTitle("TestTitle");
        patchDto.setContent("TestContent");
        Board board = boardMapper.BoardPatchDtoToBoard(patchDto);

        assertThat(board.getTitle()).isEqualTo("TestTitle");
        assertThat(board.getContent()).isEqualTo("TestContent");
    }

    @Test
    @DisplayName("Board to BoardResponseDto Test")
    void boardToBoardResponseDtoTest() {
        User user = new User("TestName", "TestPassword");
        Board board = new Board("TestTitle", "TestContent");
        board.setUser(user);

        BoardResponseDto getResponseDto = boardMapper.BoardToBoardResponseDto(board);

        assertThat(getResponseDto.getTitle()).isEqualTo(board.getTitle());
        assertThat(getResponseDto.getContent()).isEqualTo(board.getContent());
        assertThat(getResponseDto.getUsername()).isEqualTo(board.getUser().getUsername());
    }

    @Test
    @DisplayName("Boards to BoardResponseDtos Test")
    void boardsToBoardResponseDtosTest() {
        // @BeforeEach
        // entity -> dto => entity <=> dto
        User user01 = new User("TestName01", "TestPassword01");
        User user02 = new User("TestName02", "TestPassword02");
        Board board1 = new Board("TestTitle01", "TestContent01");
        Board board2 = new Board("TestTitle02", "TestContent02");
        board1.setUser(user01);
        board2.setUser(user02);

        List<Board> boards = List.of(board1, board2);

        List<BoardResponseDto> getResponseDtos = boardMapper.BoardsToBoardResponseDtos(boards);

        assertThat(getResponseDtos.size()).isEqualTo(2);
        assertThat(getResponseDtos.get(0).getContent()).isEqualTo(board1.getContent());
    }
}