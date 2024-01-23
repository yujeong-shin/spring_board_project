package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public void save(PostCreateReqDto postCreateReqDto){
        Post post = new Post(postCreateReqDto.getTitle(), postCreateReqDto.getContents());
        postRepository.save(post);
    }

    public List<PostListResDto> findAll(){
        List<Post> Posts = postRepository.findAll();
        List<PostListResDto> PostListResDtos = new ArrayList<>();
        for(Post post : Posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            PostListResDtos.add(postListResDto);
        }
        return PostListResDtos;
    }

    public Post FindById(Long id){
       Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 Post가 없습니다."));
        return post;
    }

    public PostDetailResDto findPostDetail(Long id) throws EntityNotFoundException {
        Post post = this.FindById(id);
        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        postDetailResDto.setCreatedTime(post.getCreatedTime());
        return postDetailResDto;
    }

    public void update(Long id, PostUpdateReqDto postUpdateReqDto){
        Post post = this.FindById(id);
        post.updatePost(postUpdateReqDto.getTitle(), postUpdateReqDto.getContents());
        postRepository.save(post);
    }

    public void delete(Long id){
        Post post = this.FindById(id);
        postRepository.delete(post);
    }
}
