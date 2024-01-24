package com.encore.board.post.dto;

import lombok.Data;

@Data
public class PostCreateReqDto {
    private String title;
    private String contents;
    private String email;
}
