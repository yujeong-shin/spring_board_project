package com.encore.board.author.controller;

import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RestController
public class AuthorController {
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/author/save")
//    @ResponseBody
    public String authorSave(AuthorSaveReqDto authorSaveReqDto){
        authorService.save(authorSaveReqDto);
        return "ok";
    }

    @GetMapping("/author/list")
//    @ResponseBody
    public List<AuthorListResDto> authorList() {
        return authorService.findAll();
    }

    @GetMapping("author/detail/{id}")
//    @ResponseBody
    public AuthorDetailResDto authorDetail(@PathVariable Long id){
        return authorService.findById(id);
    }


}