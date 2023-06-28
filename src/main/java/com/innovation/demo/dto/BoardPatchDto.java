package com.innovation.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardPatchDto {
    private long boardId;
    private String title;
    private String username;
    private String password;
    private String content;
}
