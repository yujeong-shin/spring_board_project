package com.encore.board.author.repository;


import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
//replace = AutoConfigureTestDatabase.Replace.ANY : H2 DB(spring 내장 인메모리)가 기본 설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//DataJpaTest 어노테이션을 사용하면 매 테스트가 종료되면 자동으로 DB 원상복구
//"모든 스프링빈을 생성하지 않고", DB 테스트 특화 어노테이션 -> @Bean 생성 시 오류
//@SpringBootTest
//@SpringBootTest 어노테이션은 자동 롤백 기능은 지원하지 않고, 별도로 롤백 코드 또는 어노테이션 필요
//실제 스프링 실행과 동일하게 스프링 빈을 생성 및 주입. 모든 스프링 빈을 생성하고 주입될 때만 사용되는 것이 차이.
//@SpringBootTest
//@Transactional
//@ActiveProfiles("test") // application-test.yml 파일을 찾아 설정값 세팅
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Test
    public void authorSaveTest(){
        // 객체를 만들어서 save -> 재조회 -> 만든 객체와 비교
        //준비(preapre, given)
        Author author = Author.builder()
                .name("test3")
                .email("test3@example.com")
                .password("test1234")
                .role(Role.ADMIN)
                .build();
        //실행(excute, when)
        authorRepository.save(author);
        Author authorDb = authorRepository.findByEmail(author.getEmail()).orElse(null);
        //검증(verify, then)
        //Assertions 클래스의 기능을 통해 오류의 원인파악, null 처리, 가시적으로 성공/실패 여부 확인
        Assertions.assertEquals(author.getEmail(), authorDb.getEmail());
//        Assertions.assertThat(authorDb.equals(author));
    }


}
