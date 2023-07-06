package com.innovation.demo.dto;

import com.innovation.demo.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private String createdAt;
    private String username;

    public CommentResponseDto(Comment comment) {
        this.commentId = comment.getCommentId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.username = comment.getUser().getUsername();
    }
}
