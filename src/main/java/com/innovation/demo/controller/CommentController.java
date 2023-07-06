package com.innovation.demo.controller;

import com.innovation.demo.dto.CommentRequestDto;
import com.innovation.demo.entity.Comment;
import com.innovation.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> postComment(@RequestHeader("Authorization") String token,
                                         @RequestBody CommentRequestDto commentRequestDto) {

        Comment createdComment = commentService.createComment(commentRequestDto, token);
        return ResponseEntity.ok(commentService.entityToResponseDto(createdComment));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> putComment(@PathVariable Long commentId,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody CommentRequestDto commentRequestDto) {
        Comment updatedComment = commentService.updateComment(commentId, commentRequestDto, token);
        return ResponseEntity.ok(commentService.entityToResponseDto(updatedComment));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,
                                           @RequestHeader("Authorization") String token) {
        commentService.deleteComment(commentId, token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
