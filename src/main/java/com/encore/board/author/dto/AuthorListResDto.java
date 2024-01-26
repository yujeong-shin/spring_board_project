package com.encore.board.author.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AuthorListResDto {
    private Long id;
    private String name;
    private String email;
}
