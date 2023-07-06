package com.innovation.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentRequestDto {
    private String content;
    private Long boardId;

}
