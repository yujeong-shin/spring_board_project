package com.encore.board.post.service;

import com.encore.board.post.domain.Post;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
public class PostScheduler {
    private final PostRepository postRepository;
    @Autowired
    public PostScheduler(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    // 초 분 시간 일 월 요일 형태로 스케줄링 설정
    // * : 매 초(분/시 등)을 의미, 0/특정숫자 : 특정숫자마다 반복
    // 특정 숫자 : 특정 숫자의 초(분/시 등)을 의미
    // 초 분 시 일 월 요일
    // 0 0 * * * * : 매일 0분 0초에 스케줄링 시작
    // 0 0/1 * * * * : 매일 1분마다 0초에 스케줄링 시작
    // 0/1 * * * * * : 매초마다
    // 0 0 11 * * * : 매일 11시에 스케줄링
    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional //한 트랜잭션의 끝에 commit하는데, commit이 save()와 같은 개념이라 자동 더티체킹된다 ⭐
    public void postSchedule(){
        System.out.println("===스케줄러 시작===");
        Page<Post> posts = postRepository.findByAppointment("Y", Pageable.unpaged());
        for(Post post : posts.getContent()){
            if(post.getAppointmentTime().isBefore(LocalDateTime.now())){
                post.updateAppointment(null);
//                postRepository.save(post);
            }
        }
        System.out.println("===스케줄러 끝===");
    }
}