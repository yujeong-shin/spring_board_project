package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostCreateReqDto;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;
    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostCreateReqDto postCreateReqDto, String email) throws IllegalArgumentException{
//        // 로그인 된 사용자의 email 값을 받아서 저장
//        // Controller에서 전달받지 않고, Service 단에서 session 값을 꺼낼 때 사용
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        Author author = authorRepository.findByEmail(email).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if (postCreateReqDto.getAppointment().equals("Y") &&  //YES인 경우에만 DB에 Y, NO이면 null 세팅
                !postCreateReqDto.getAppointmentTime().isEmpty()){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            localDateTime = LocalDateTime.parse(postCreateReqDto.getAppointmentTime() , dateTimeFormatter);
            LocalDateTime now = LocalDateTime.now();
            if(localDateTime.isBefore(now)){
                throw new IllegalArgumentException("시간정보 잘못입력");
            }
            appointment = "Y";
        }
        Post post = Post.builder()
                .title(postCreateReqDto.getTitle())
                .contents(postCreateReqDto.getContents())
                .author(author)
                .appointment(appointment)
                .appointmentTime(localDateTime)
                .build();
//        더티체킹 테스트
//        author.updateAuthor("dirty checking test", "1234");
//        authorRepository.save(author);

        postRepository.save(post);
    }

    public List<PostListResDto> findAll(){
        List<Post> Posts = postRepository.findAllFetchJoin();
//        List<Post> Posts = postRepository.findAll();
        List<PostListResDto> PostListResDtos = new ArrayList<>();
        for(Post post : Posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            postListResDto.setAuthor_email(post.getAuthor()==null?"익명유저":post.getAuthor().getEmail());
//            postListResDto.setAuthor_email(post.getAuthor().getEmail()); //⭐ post객체에 있는 author_id로 Author 테이블에서 꺼내온 autohr 객체
            PostListResDtos.add(postListResDto);
        }
        return PostListResDtos;
    }

    public Page<PostListResDto> findByAppointment(Pageable pageable){
        Page<Post> posts = postRepository.findByAppointment(null, pageable);
        Page<PostListResDto> PostListResDtos
                = posts.map(post -> new PostListResDto(post.getId(), post.getTitle(), post.getAuthor()==null?"익명유저":post.getAuthor().getEmail()));
        return PostListResDtos;
    }

    public Page<PostListResDto> findAllPaging(Pageable pageable){
        Page<Post> posts = postRepository.findAll(pageable);
        Page<PostListResDto> PostListResDtos
                = posts.map(post -> new PostListResDto(post.getId(), post.getTitle(), post.getAuthor()==null?"익명유저":post.getAuthor().getEmail()));
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
