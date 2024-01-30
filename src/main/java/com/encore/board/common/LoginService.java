package com.encore.board.common;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoginService implements UserDetailsService {
    private final AuthorRepository authorRepository;

    @Autowired
    public LoginService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    //UserDetails : interface, User() : UserDetails를 구현해 객체 생성
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("검색하신 EMAIL의 Member가 없습니다."));

        // "회원관리" 페이지를 ADMIN 권한을 가진 사용자만 접근할 수 있게 변경
        // authorities에 사용자의 role 정보를 삽입해 User 객체 생성
        List<GrantedAuthority> authorities = new ArrayList<>(); //권한은 multiple
        //authorities.add(new SimpleGrantedAuthority(author.getRole().toString()));
        //ROLE_권한 : 패턴으로 스프링에서 기본적으로 권한 체크, 애초부터 이 형식으로 만들어줌
        authorities.add(new SimpleGrantedAuthority("ROLE_"+author.getRole()));

        //매개변수 : userEmail, userPassword, 권한(authorities)
        //해당 메서드에서 return 되는 User 객체는 session 메모리 저장소에 저장되어, 계속 사용 가능
        return new User(author.getEmail(), author.getPassword(), authorities);
    }
}
