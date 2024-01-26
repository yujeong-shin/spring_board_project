package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class AuthorServiceTest {
    //가짜 Repository를 만들어 Service 로직을 테스트

    //진짜 객체
    @Autowired
    private AuthorService authorService;

    //가짜 객체를 만드는 작업을 목킹이라 한다.
    @MockBean
    private AuthorRepository authorRepository;

    @Test
    void updateTest(){
        Long authorId = 1L;
        Author author = Author.builder()
                .name("test1")
                .email("test1@example.com")
                .password("test1111")
                .build();
        //가짜 메서드 정의 후, 호출 시 가짜 객체 리턴
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorUpdateReqDto authorUpdateReqDto = new AuthorUpdateReqDto();
        authorUpdateReqDto.setName("test2");
        authorUpdateReqDto.setPassword("test2222");

        //검증 : Author.update() 리턴값이 void
        authorService.update(authorId, authorUpdateReqDto);
        Assertions.assertEquals(author.getName(), authorUpdateReqDto.getName());
        Assertions.assertEquals(author.getPassword(), authorUpdateReqDto.getPassword());

//        //검증 - 재영ver : Author.update() 리턴값이 Author
//        Author author_new = authorService.update(authorId, authorUpdateReqDto); //Mockito 없으니 진짜 객체의 update 실행
//        Assertions.assertEquals(author_new.getName(), authorUpdateReqDto.getName());
//        Assertions.assertEquals(author_new.getPassword(), authorUpdateReqDto.getPassword());
    }

    @Test
    void findAllTest(){
        // Mock repository 기능 구현
        List<Author> authors = new ArrayList<>();
        authors.add(new Author());
        authors.add(new Author());
        //원래는 DB를 조회해 회원 목록을 가져오는데, 이는 목적 적합하기 않기 때문에 가짜 객체로 대체해 테스트
        Mockito.when(authorRepository.findAll()).thenReturn(authors);

        for(Author author : authorRepository.findAll()) {
            System.out.println(author.getId());
        }
        //검증
        Assertions.assertEquals(authors.size(), authorRepository.findAll().size());
    }

    @Test
    void findAuthorDetailTest(){
        Long authorId = 1L;
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder()
                .id(1L)
                .title("test1")
                .contents("test 1 is ...")
                .build();
        posts.add(post);
        Author author = Author.builder()
                .id(authorId)
                .name("test1")
                .email("test1@example.com")
                .password("test1111")
                .posts(posts)
                .build();
        //가짜 메서드 정의 후, 호출 시 가짜 객체 리턴
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        AuthorDetailResDto authorDetailResDto = authorService.findAuthorDetail(authorId);
        Assertions.assertEquals(author.getName(), authorDetailResDto.getName());
        Assertions.assertEquals(author.getPosts().size(), authorDetailResDto.getCounts());
//        Assertions.assertEquals("유저", authorDetailResDto.getRole()); //디폴트 값은 "일반유저"
        Assertions.assertEquals("일반유저", authorDetailResDto.getRole());
    }
}
