package com.encore.board.author.controller;

import com.encore.board.author.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
//Slf4j어노테이션(롬복)을 통해 쉽세 logback 로그 라이브러리 사용가능
@RestController
public class TestController {
    @Autowired
    private AuthorService authorService;

//    //Slf4j 어노테이션 사용하지 않고, 직접 라이브러리 import하여 로거 생성 가능
//    private static final Logger log = LoggerFactory.getLogger(LogTestController.class);
    @GetMapping("log/test1")
    public String testMethod (){
        log.debug("debug 로그입니다.");
        log.info("info 로그입니다.");
        log.error("error 로그입니다.");
        return "ok";
    }

    @GetMapping("exception/test1/{id}")
    public String exceptionTestMethod (@PathVariable Long id){
        authorService.findById(id);
        return "ok";
    }

//    controller에서 이미 try-catch문으로 잡고 있으면 여기서 안잡힘
//    @GetMapping("exception/test1/{id}")
//    public String exceptionTestMethod (@PathVariable Long id){
//        try{
//            authorService.findById(id);
//        } catch(EntityNotFoundException e){
//            log.error("Handler EntityNotFoundException message : " + e.getMessage());
//        }
//        return "ok";
//    }
}