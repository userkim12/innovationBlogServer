package com.innovation.demo.service;

import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.entity.User;
import com.innovation.demo.mapper.BoardMapper;
import com.innovation.demo.repository.BoardRepository;
import com.innovation.demo.repository.UserRepository;
import com.innovation.demo.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardMapper boardMapper;
    private final JwtUtil jwtUtil;

    @Value("${token.name.Authorization}")
    private String LoginToken;

    @Value("${admin.username}")
    private String adminName;

    public Board createBoard(BoardPostDto boardPostDto, String token) {
        String username = jwtUtil.getUsernameFromToken(token);

        Board board = new Board(boardPostDto.getTitle(), boardPostDto.getContent());

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        user.addBoard(board);

        return board;
    }


    //    @Transactional
    public Board findBoard(long boardId) {

        return findVerifiedBoard(boardId);
    }


    //    @Transactional(readOnly = true)
    public List<Board> findAllBoards() {

        return boardRepository.findAllByOrderByCreatedAtDesc();
    }

    public Board updateBoard(long boardId, BoardPatchDto boardPatchDto, String token) {
        Board board = boardMapper.BoardPatchDtoToBoard(boardPatchDto);
        Board findBoard = findVerifiedBoard(boardId);

        String username1 = findBoard.getUser().getUsername();
        String username2 = jwtUtil.getUsernameFromToken(token);

        if(!username2.equals(adminName)) {
            compareUsernames(username1, username2);
        }

        Optional.ofNullable(board.getTitle()).ifPresent(findBoard::setTitle);
        Optional.ofNullable(board.getContent()).ifPresent(findBoard::setContent);

        return boardRepository.save(findBoard);
    }


    public void deleteBoard(long boardId, String token) {
        Board findBoard = findVerifiedBoard(boardId);

        String username1 = findBoard.getUser().getUsername();
        String username2 = jwtUtil.getUsernameFromToken(token);

        if(!username2.equals(adminName)) {
            compareUsernames(username1, username2);
        }

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

    private void compareUsernames(String username1, String username2) {
        if (!username1.equals(username2))
            throw new RuntimeException("작성자만 삭제/수정할 수 있습니다.");
    }

    private Board findVerifiedBoard(long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
                () -> new RuntimeException("데이터가 존재하지 않습니다.")
        );
    }
}

