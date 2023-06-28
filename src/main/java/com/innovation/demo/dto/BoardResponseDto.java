package com.innovation.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {
    private long boardId;
    private String title;
    private String username;
    private String content;
    private String createdAt;
}
