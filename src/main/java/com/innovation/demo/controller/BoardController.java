package com.innovation.demo.controller;

import com.innovation.demo.dto.BoardDeleteDto;
import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/board")
    public ResponseEntity<?> postBoard(@RequestBody BoardPostDto boardPostDto) {
        Board savedBoard = boardService.createBoard(boardPostDto);

        BoardResponseDto response = boardService.entityToResponseDto(savedBoard);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/board/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable long boardId) {
        Board board = boardService.findBoard(boardId);

        BoardResponseDto response = boardService.entityToResponseDto(board);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/boards")
    public ResponseEntity<?> getAllBoards() {
        List<Board> boards = boardService.findAllBoards();

        List<BoardResponseDto> responses = boardService.entityListToResponseDtoList(boards);
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @PatchMapping("/board/{boardId}")
    public ResponseEntity<?> patchBoard(@PathVariable long boardId,
                                        @RequestBody BoardPatchDto boardPatchDto) {

        Board patchedcBoard = boardService.updateBoard(boardId, boardPatchDto);

        BoardResponseDto response = boardService.entityToResponseDto(patchedcBoard);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<?> deleteBoard(@PathVariable long boardId,
                                         @RequestBody BoardDeleteDto boardDeleteDto) {

        String password = boardDeleteDto.getPassword();
        boardService.deleteBoard(boardId, boardDeleteDto);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
