package com.encore.board.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

//ControllerAdvice와 ExceptionHandler를 통해 예외 처리의 공통화 로직 작성
@ControllerAdvice
@Slf4j
public class CommonException {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundHandler(EntityNotFoundException e){
        log.error("Handler EntityNotFoundException message : " + e.getMessage());
        return this.errResponseMessage(HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> illegalArguHandler(IllegalArgumentException e){
        log.error("Handler EntityNotFoundException message : " + e.getMessage());
        return this.errResponseMessage(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    private ResponseEntity<Map<String, Object>> errResponseMessage(HttpStatus status, String message){
        Map<String, Object> body = new HashMap<>();
        body.put("status", Integer.toString(status.value())); //responseBody에 출력되도록 넣기
        body.put("status message", status.getReasonPhrase());
        body.put("error message", message);
        return new ResponseEntity<>(body, status); //status에 담긴 값은 header로 나가고, body에 담긴 Map은 json 형태로 나감.
    }
}
