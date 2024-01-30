package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public AuthorService(AuthorRepository authorRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) throws IllegalArgumentException {
        if(authorRepository.findByEmail(authorSaveReqDto.getEmail()).isPresent())
            throw new IllegalArgumentException("중복 이메일");

        Role role = null;
        if(authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")){
            role = Role.USER;
        }else{
            role = Role.ADMIN;
        }
        //일반 생성자 방식
        //Author author = new Author(authorSaveReqDto.getName(), authorSaveReqDto.getEmail(), authorSaveReqDto.getPassword(), role);


        //빌더패턴
        // .build() : 최종적으로 완성시키는 단계
        Author author = Author.builder()
                .email(authorSaveReqDto.getEmail())
                .name(authorSaveReqDto.getName())
                .password(passwordEncoder.encode(authorSaveReqDto.getPassword()))
                .role(role)
                .build();

//        //cascade.persist 테스트
//        //부모 테이블을 통해 자식 테이블에 객체를 동시에 생성
//        List<Post> posts = new ArrayList<>();
//        Post post = new Post.builder()
//                .title("안녕하세요. " + author.getName() + "입니다.")
//                .contents("반갑습니다. cascade 테스트 중입니다..")
//                .author(author)
//                .build();
//        posts.add(post);
        //author.setPosts(posts); // setter를 사용하지 않기 위해 Post 생성자에 this.author.getPosts(this);
        //위 post 생성 주석을 풀 경우, author만 save해줘도 post까지 save됨 => cascade ⭐
        authorRepository.save(author);
    }

    public List<AuthorListResDto> findAll() {
        List<Author> Authors = authorRepository.findAll();
        List<AuthorListResDto> AuthorListResDtos = new ArrayList<>();
        for(Author author : Authors){
            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(author.getId());
            authorListResDto.setName(author.getName());
            authorListResDto.setEmail(author.getEmail());
            AuthorListResDtos.add(authorListResDto);
        }
        return AuthorListResDtos;
//        return authorRepository.findAll().stream().map(author -> new AuthorListResDto(author.getId(), author.getName(), author.getEmail())).toList();
    }

    public Author findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 Member가 없습니다."));
        return author;
    }

//    public Author findByEmail(String email) throws EntityNotFoundException{
//        Author author = authorRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("검색하신 EMAIL의 Member가 없습니다."));
//        return author;
//    }

    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = this.findById(id);
        String role = null;
        if(author.getRole() == null || author.getRole().equals(Role.USER)){
            role = "일반유저";
        }else{
            role = "관리자";
        }
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setRole(role);
        authorDetailResDto.setCounts(author.getPosts().size());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        return authorDetailResDto;
    }
    public void update(Long id, AuthorUpdateReqDto authorUpdateReqDto) throws EntityNotFoundException {
        Author author = this.findById(id);
        author.updateAuthor(authorUpdateReqDto.getName(), authorUpdateReqDto.getPassword());
        //명시적으로 save를 하지 않더라도, JPA의 영속성 컨텍스트를 통해
        //객체에 변경이 감지(dirty checking)되면, 트랜잭션이 완료되는 시점에 save 동작
        authorRepository.save(author);
    }

    public void delete(Long id) throws EntityNotFoundException {
        Author author = this.findById(id);
        authorRepository.delete(author);
//        authorRepository.deleteById(id);
    }

}
