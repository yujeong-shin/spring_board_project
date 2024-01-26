package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 50)
    private String title;
    @Column(nullable = false, length = 3000)
    private String contents;
    private String appointment;
    private LocalDateTime appointmentTime;
    //author_id는 DB의 컬럼명, 별다른 옵션 없는 경우 author의 PK에 FK가 설정
    //DB에서는 Post 테이블에 author_id로 관리, JAVA에서는 author 객체로 관리
    //post 객체 입장에서는 한사람이 여러개 글을 쓸 수 있으므로 N:1
    @ManyToOne(fetch = FetchType.LAZY) // 관계성 JPA에게 알리기
    @JoinColumn(name = "author_id")
    //@JoinColumn(nullable=false, name = "author_email", referencedColumnName = "email")
    private Author author;

    @CreationTimestamp
    // 개발자가 DB를 바꾸는 게 risky한 것. 프로그램적으로 다루는 것이 좋다.
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime updatedTime;
//    @Builder
//    public Post(String title, String contents, Author author, String appointment, LocalDateTime appointmentTime){
//        this.title = title;
//        this.contents = contents;
//        this.author = author;
//        this.appointment = appointment;
//        this.appointmentTime = appointmentTime;
////        //author 객체의 posts를 초기화 시켜준 후
////        this.author.getPosts().add(this);
//    }
    public void updatePost(String title, String contents){
        this.title = title;
        this.contents = contents;
    }

    public void updateAppointment(String appointment){
        this.appointment = appointment;
    }
}
