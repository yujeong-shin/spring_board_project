package com.encore.board.post.controller;

import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("post/create")
    public String postCreate() {
        return "post/post-create";
    }
    @PostMapping("post/create")
    public String postSave(PostCreateReqDto postCreateReqDto){
        postService.save(postCreateReqDto);
        return "redirect:/post/list";
    }

    @GetMapping("post/list")
    public String postList(Model model) {
        model.addAttribute("postList", postService.findAll());
        return "post/post-list";
//        return postService.findAll();
    }

    @GetMapping("post/detail/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        model.addAttribute("post", postService.findPostDetail(id));
        return "post/post-detail";
//        return postService.findPostDetail(id);
    }

    @PostMapping("post/{id}/update")
    public String postUpdate(@PathVariable Long id, PostUpdateReqDto postUpdateReqDto, Model model) {
        postService.update(id, postUpdateReqDto);
        model.addAttribute("post", postUpdateReqDto);
        return "redirect:/post/detail/" + id;
    }

    @GetMapping("post/delete/{id}")
    public String postDelete(@PathVariable Long id){
        postService.delete(id);
        return "redirect:/post/list";
    }
}
