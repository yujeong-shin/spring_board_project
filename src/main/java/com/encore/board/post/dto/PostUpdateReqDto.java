package com.encore.board.post.dto;

import lombok.Data;

@Data
public class PostUpdateReqDto {
    private String title;
    private String contents;
}
