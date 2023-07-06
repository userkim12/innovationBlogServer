package com.innovation.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BoardResponseDto {
    private long boardId;
    private String title;
    private String username;
    private String content;
    private String createdAt;

    private List<CommentResponseDto> comments;
}