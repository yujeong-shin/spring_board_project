package com.encore.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Aspect
@Component
@Slf4j
public class AopLogService {
    //aop의 대상이 되는 controller, service 등 위치를 정의
    //모든 컨트롤러의 모든 메서드가 대상 : 패키지명과 메서드명의 표현식
//    @Pointcut("excution(* com.encore.board..controller..*.*(..)")
    //모든 컨트롤러의 모든 메서드가 대상 : 어노테이션 표현식
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointcut(){

    }

//    // 방식1. before + after 사용
//    @Before("controllerPointcut()") // Pointcut에서 정의한 위치를 대상으로 하겠다
//    public void beforeController(JoinPoint joinPoint){
//        log.info("Before Controller");
////        메서드가 실행되기 전에 인증, 입력 값 검증 등을 수행하는 용도로 사용하는 사전단계
//
//        //사용자의 요청 값을 출력하기 위해 httpServletRequest 객체를 꺼내는 로직
//        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest req = servletRequestAttributes.getRequest();
//
//        //json 형태로 사용자의 요청을 조립하기 위한 로직
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.createObjectNode();
//        objectNode.put("Method Name", joinPoint.getSignature().getName());
//        objectNode.put("CRUD Name", req.getMethod());
//        // aop가 뺏은 메서드와 메서드 요청방식 정보
//
//        Map<String, String[]> paramMap = req.getParameterMap();
//        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
//        objectNode.set("user inputs", objectNodeDetail);
//        log.info("user request info" + objectNode);
//    }
//
//    @After("controllerPointcut()")
//    public void afterController(){
//        log.info("end controller");
//    }

    //방식2. around 사용(가장 빈번히 사용)
    @Around("controllerPointcut()") // 낚아챈 일을 다 수행하고, 요청 들어온 본래 컨트롤러의 메서드로 보냄
    //join point란 AOP 대상으로 하는 컨트롤러의 특정 메서드를 의미
    public Object controllerLogger(ProceedingJoinPoint proceedingJoinPoint){
//        log.info("request method" + proceedingJoinPoint.getSignature().toString()); //어떤 클래스의 어떤 메서드를 호출하는지 출력

        //사용자의 요청 값을 출력하기 위해 httpServletRequest 객체를 꺼내는 로직
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = servletRequestAttributes.getRequest();

        //json 형태로 사용자의 요청을 조립하기 위한 로직
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("Method Name", proceedingJoinPoint.getSignature().getName());
        objectNode.put("CRUD Name", req.getMethod());
        // aop가 뺏은 메서드와 메서드 요청방식 정보
        // {"Method Name" : home()}
        // {"CRUD Name" : GET}

        Map<String, String[]> paramMap = req.getParameterMap();
        ObjectNode objectNodeDetail = objectMapper.valueToTree(paramMap);
        objectNode.set("user inputs", objectNodeDetail);
        log.info("user request info" + objectNode);
        // aop가 뺏은 곳의 사용자 입력값들
        //{user inputs" : {"a": a, "b": b, "c": c}}
        //여기까지가 @Before 역할

        try {
            //본래의 컨트롤러 메서드 호출하는 부분.
            return proceedingJoinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        } finally { //여기가 @After 역할
            log.info("end controller");
        }
    }
}
