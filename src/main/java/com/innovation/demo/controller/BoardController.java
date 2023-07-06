package com.innovation.demo.controller;

import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<?> postBoard(@RequestBody BoardPostDto boardPostDto, @RequestHeader("Authorization") String token) {
        Board savedBoard = boardService.createBoard(boardPostDto, token);

        BoardResponseDto response = boardService.entityToResponseDto(savedBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable long boardId) {
        Board board = boardService.findBoard(boardId);

        BoardResponseDto response = boardService.entityToResponseDto(board);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/boards")
    public ResponseEntity<?> getAllBoards() {
        List<Board> boards = boardService.findAllBoards();

        List<BoardResponseDto> responses = boardService.entityListToResponseDtoList(boards);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/board/{boardId}")
    public ResponseEntity<?> patchBoard(@PathVariable Long boardId,
                                        @RequestBody BoardPatchDto boardPatchDto,
                                        @RequestHeader("Authorization") String token) {

        Board patchedcBoard = boardService.updateBoard(boardId, boardPatchDto, token);

        BoardResponseDto response = boardService.entityToResponseDto(patchedcBoard);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long boardId,
                                         @RequestHeader("Authorization") String token) {


        boardService.deleteBoard(boardId, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
