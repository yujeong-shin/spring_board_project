package com.encore.board.post.controller;

import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
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
    public String postSave(Model model, PostCreateReqDto postCreateReqDto){
        try{
            postService.save(postCreateReqDto);
            return "redirect:/post/list";
        } catch(IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            log.error(e.getMessage());
            return "post/post-create";
        }

    }

    @GetMapping("post/list")
    public String postList(Model model, @PageableDefault(size=5, sort="createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("postList", postService.findByAppointment(pageable));
        return "post/post-list";
    }

//    @GetMapping("json/post/list")
//    @ResponseBody
//    //localhost:8080/post/list?page=1&size=10&sort=createdTime,desc
//    public Page<PostListResDto> postList(Pageable pageable) { //Pageable : Page 객체를 조회하기 위한 매개변수
//        // Page<Post>로 반환되는 데이터들을 PostListResDto로 변환하여 순환참조 풀어주기
//        return postService.findAllJson(pageable);
//    }

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
