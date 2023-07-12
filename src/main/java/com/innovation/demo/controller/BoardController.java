package com.innovation.demo.controller;

import com.innovation.demo.dto.BoardPatchDto;
import com.innovation.demo.dto.BoardPostDto;
import com.innovation.demo.dto.BoardResponseDto;
import com.innovation.demo.entity.Board;
import com.innovation.demo.security.UserDetailsImpl;
import com.innovation.demo.service.BoardService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> postBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                       @RequestBody BoardPostDto boardPostDto) {
        Board savedBoard = boardService.createBoard(userDetails, boardPostDto);

        BoardResponseDto response = boardService.entityToResponseDto(savedBoard);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<?> getBoard(@PathVariable long boardId) {
        Board board = boardService.findBoard(boardId);

        BoardResponseDto response = boardService.entityToResponseDto(board);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping
    public ResponseEntity<?> getAllBoards() {
        List<Board> boards = boardService.findAllBoards();

        List<BoardResponseDto> responses = boardService.entityListToResponseDtoList(boards);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<?> patchBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable Long boardId,
                                        @RequestBody BoardPatchDto boardPatchDto) {

        Board patchedcBoard = boardService.updateBoard(boardId, boardPatchDto, userDetails);

        BoardResponseDto response = boardService.entityToResponseDto(patchedcBoard);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                         @PathVariable Long boardId) {


        boardService.deleteBoard(boardId, userDetails);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
