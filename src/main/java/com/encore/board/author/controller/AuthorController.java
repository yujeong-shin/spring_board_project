package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class AuthorController {
    private final AuthorService authorService;
    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("author/create")
    public String authorCreate(){
        // 에러가 발생한 채로 redirect 된건지, 정상적인 페이지 접근인지 구별해야 함.
        return "author/author-create";
    }

    @PostMapping("author/create")
    public String authorSave(Model model, AuthorSaveReqDto authorSaveReqDto){
        try{
            authorService.save(authorSaveReqDto);
            return "redirect:/author/list";
        }catch(IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            log.error(e.getMessage());
            return "author/author-create";
            //회원가입 시 에러가 발생하면 redirect말고 화면 넘김.
            //redirect에는 model에 값을 꽂을 수 없다.
        }
    }

    @GetMapping("author/list")
    public String authorList(Model model) {
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";
    }

    @GetMapping("author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model){
        System.out.println(id);
        model.addAttribute("author", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

    @PostMapping("author/{id}/update")
    public String authorUpdate(@PathVariable Long id, AuthorUpdateReqDto authorUpdateReqDto, Model model) {
        authorService.update(id, authorUpdateReqDto);
        model.addAttribute("author", authorUpdateReqDto);
        return "redirect:/author/detail/" + id;
    }

    @GetMapping("author/delete/{id}")
    public String authorDelete(@PathVariable Long id){
        authorService.delete(id);
        return "redirect:/author/list";
    }

    // 직렬화 순환참조 이슈
    @GetMapping("author/{id}/circle/entity")
    @ResponseBody
    //연관관계가 있는 Author 엔티티를 json으로 직렬화를 하게 될 경우
    //순환참조 이슈 발생하므로, DTO 사용 필요.
    public Author circleIssueTest1(@PathVariable Long id){
        return authorService.findById(id);
    }

    @GetMapping("author/{id}/circle/dto")
    @ResponseBody
    public AuthorDetailResDto circleIssueTest2(@PathVariable Long id){
        return authorService.findAuthorDetail(id);
    }
}
