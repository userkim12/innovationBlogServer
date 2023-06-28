package com.innovation.demo.service;

import com.innovation.demo.dto.BoardDeleteDto;
import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.mapper.BoardMapper;
import com.innovation.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public Board createBoard(BoardPostDto boardPostDto) {
        Board board = boardMapper.BoardPostDtoToBoard(boardPostDto);
        validateBoardData(board);

        return boardRepository.save(board);
    }



    public Board findBoard(long boardId) {

        return findVerifiedBoard(boardId);
    }


    public List<Board> findAllBoards() {

        return boardRepository.findAllByOrderByCreatedAtDesc();
    }

    public Board updateBoard(long boardId, BoardPatchDto boardPatchDto) {
        Board board = boardMapper.BoardPatchDtoToBoard(boardPatchDto);
        Board findBoard = findVerifiedBoard(board.getBoardId());

        String inputPassword = board.getPassword();
        String password = findBoard.getPassword();

        comparePasswords(password, inputPassword);

        Optional.ofNullable(board.getTitle()).ifPresent(findBoard::setTitle);
        Optional.ofNullable(board.getContent()).ifPresent(findBoard::setContent);
        Optional.ofNullable(board.getUsername()).ifPresent(findBoard::setUsername);

        return boardRepository.save(findBoard);
    }


    public void deleteBoard(long boardId, BoardDeleteDto boardDeleteDto) {
        Board findBoard = findVerifiedBoard(boardId);
        String password = findBoard.getPassword();
        String inputPassword = boardDeleteDto.getPassword();

        comparePasswords(password, inputPassword);

        boardRepository.delete(findBoard);
    }

    public BoardResponseDto entityToResponseDto(Board board) {

        return boardMapper.BoardToBoardResponseDto(board);
    }

    public List<BoardResponseDto> entityListToResponseDtoList(List<Board> boards) {

        return boardMapper.BoardsToBoardResponseDtos(boards);
    }


    private void validateBoardData(Board board) {
        Map<String, String> fields = new HashMap<String, String>() {{
            put("비밀번호", board.getPassword());
            put("이름", board.getUsername());
            put("제목", board.getTitle());
            put("내용", board.getContent());
        }};

        List<String> errors = new ArrayList<>();

        fields.forEach((key, value) -> {
            if (StringUtils.isEmpty(value)) {
                errors.add(key + "를 입력해주세요.");
            }
        });

        if (!errors.isEmpty()) {
            throw new RuntimeException(String.join("\n", errors));
        }
    }

    private void comparePasswords(String password, String inputPassword) {
        if(!password.equals(inputPassword))
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
    }

    private Board findVerifiedBoard(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("데이터가 존재하지 않습니다.")
        );
    }
}
