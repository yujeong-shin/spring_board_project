package com.encore.board.common;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    // 로그인이 성공했을 때 실행되는 메서드
    // 사용자의 request 객체에서 session 객체를 꺼내, 세션 저장소인 HttpSession에 저장
    // 모든 파일에서 HttpSession은 접근 가능하기 떄문에, 전역적으로 사용 가능
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();
        //authentication 객체 안에는 User 객체가 들어가 있고, 여기서 getName은 email을 의미
        //authentication.getName() : 인증된 사용자의 email
        //setAttribute를 사용해 별도로 저장해주지 않아도, 어디에서든 authentication 객체에서 값을 꺼낼 수 있음
        //login 성공 시점에 User 객체가 만들어져 자동으로 authentication 객체에 들어가기 때문
        httpSession.setAttribute("email", authentication.getName());
        response.sendRedirect("/");
    }
}