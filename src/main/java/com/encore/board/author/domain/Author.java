package com.encore.board.author.domain;

import com.encore.board.post.domain.Post;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Builder
// Builder 어노테이션을 통해 빌더 패턴으로 객체 생성
// 매개변수의 순서, 개수 등을 유연하게 세팅
@AllArgsConstructor
//위와 같이 모든 매개변수가 있는 생성자를 생성하는 어노테이션과 Builder를 클래스에 붙여
//모든 매개변수가 있는 생성자 위에 Builder 어노테이션과 붙인 것과 같은 효과가 있음.
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    //author를 조회할 때 post 객체가 필요할 시에 선언
    //mappedBy를 연관관계의 주인을 명시하고, FK를 관리하는 변수명을 명시
    //1:1 관계일 경우 @OneToOne도 존재 (USER-ROLE, USER-USERDETAIL)
    //연관관계 주인이 아닌 컬럼의 경우, 꼭 posts를 사용하지 않을 겨우 @OneToMany 생략가능. 가져다가 쓸 경우에만 사용
    //하지만 연관관계 주인인 post 쪽은 @JoinColumn을 꼭 설정해줘야 한다(FK를 걸기 위해). 두 개가 양방향이 아니라는 것
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch=FetchType.LAZY) //Post 객체에 있는 변수명을 적어 매핑관계 표현
    private List<Post> posts; // posts 리스트에 post가 생성될 때마다 Post 테이블 가서 생성해줌
    //AuthorRepository.save만 해줘도 자동으로 PostRepository.save까지 해줌

    @CreationTimestamp
    // 개발자가 DB를 바꾸는 게 risky한 것. 프로그램적으로 다루는 것이 좋다.
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;

// @Builder // 클래스 단에 붙여주지 않으면 메서드 단에서 설정 가능
//    public Author(String name, String email, String password, Role role, List<Post> posts) {
//        this.name = name;
//        this.email = email;
//        this.password = password;
//        this.role = role;
//        this.posts = posts;
//    }

    public void updateAuthor(String name, String password){
        this.name = name;
        this.password = password;
    }
}