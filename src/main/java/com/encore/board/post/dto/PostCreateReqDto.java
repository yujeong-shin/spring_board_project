package com.encore.board.post.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class PostCreateReqDto {
    private String title;
    private String contents;
    private String email;
    private String appointment;
    @NonNull
    private String appointmentTime;
}
