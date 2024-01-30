package com.encore.board.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //해당 어노테이션은 spring security 설정을 customizing 하기 위함
//WebSecurityConfigurerAdapter를 상속하는 방식은 deprecated(지원종료)되었다.
@EnableGlobalMethodSecurity(prePostEnabled = true)
//pre : 사전, post : 사후, 사전/사후에 인증/권한 검사 어노테이션 사용 가능
public class SecurityConfig {
    //spring 기본 필터를 무시하고, 우리가 커스텀한 필터를 기본으로 사용하겠다
    @Bean
    public SecurityFilterChain myFilter(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            //csrf 보안 공격에 대한 설정은 하지 않겠다는 의미
            .csrf().disable()
            //특정 url에 대해서는 인증처리 하지 않고, 특정 url에 대해서는 인증처리 하겠다라는 설정
            .authorizeRequests()
                    //인증 미적용 url 패턴
                    .antMatchers("/", "/author/create", "/author/login-page")
                        .permitAll()
                    //그 외 요청은 모두 인증 필요
                . anyRequest().authenticated()
                .and()
                //만약 세션 방식을 사용하지 않으면 아래 내용 설정, 토큰 방식 사용 시 표기해주자
                //.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .formLogin()
                    //개발한 login page로 home 설정
                    .loginPage("/author/login-page")
                //스프링 내장 메서드를 사용하기 위해 /doLogin url 사용
                    .loginProcessingUrl("/doLogin")
                        .usernameParameter("email") //username : 사용자 이름이 아닌, 유일한 키 값
                        .passwordParameter("pw")
                //로그인이 성공했을 때 처리할 로직
                .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                //spring security의 doLogout 기능 그대로 사용
                    .logoutUrl("/doLogout")
                    .logoutSuccessUrl("/")
                .and()
                .build();
    }
}
