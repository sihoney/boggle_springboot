package com.boggle.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.boggle.example.service.CustomUserDetailsService;

@Configuration // 설정을 정의하는 클래스
@EnableWebSecurity //Spring Security 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
//    private UserDetailsService userDetailsService;
	
//    HttpSecurity: 웹 어플리케이션의 HTTP 보안을 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests() //요청에 대한 접근 권한을 설정하는 메서드
//                .antMatchers("/**/mybook").authenticated() // /mybook 경로에 대한 요청은 인증된 사용자만 접근할 수 있습니다.
//                .antMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한이 있는 사용자에게만 허용
//                .anyRequest().permitAll() // 그 외의 모든 요청은 인증 없이 접근할 수 있습니다.   
//                .antMatchers("/user/loginForm", "/user/joinForm", "/boggle").permitAll() // 로그인 페이지는 인증 없이 접근 가능
//                .anyRequest().authenticated()
            	  .anyRequest().permitAll()
                .and()
            .formLogin() // 로그인 설정
                .loginPage("/user/loginForm") //사용자 정의 로그인 페이지의 경로를 설정
                .loginProcessingUrl("/login") // 로그인 처리 URL 설정 (선택 사항)
                .defaultSuccessUrl("/boggle", true) // 로그인 성공 시 리다이렉트할 URL
                .failureUrl("/user/loginForm?error=true") // 로그인 실패 시 리다이렉트할 URL
                .permitAll()
                .and()    
//            .logout() // 로그아웃 설정
//            	.logoutUrl("/logout")
//	            .logoutSuccessUrl("/user/loginForm")
//                .permitAll() //로그아웃에 모든 사용자에게 접근 권한을 부여
//                .and()
            .sessionManagement() // 세션 관리
                .sessionFixation().migrateSession() // 세션 고정 보호 전략 설정
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요한 경우에만 세션 생성
                .invalidSessionUrl("/user/loginForm") // 세션이 만료된 경우 리다이렉트할 URL
                .maximumSessions(1) // 최대 동시 세션 수
                .maxSessionsPreventsLogin(true) // 새로운 로그인 시 이전 세션 만료 여부
                .and()
            .and()
            .exceptionHandling() // 예외 처리
                .accessDeniedPage("/access-denied") // 권한이 부족한 사용자가 접근하려고 할 때 리다이렉트할 페이지
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증되지 않은 사용자가 접근할 때 401 Unauthorized 응답을 보냅니다.
                .and()
            .csrf().disable(); // CSRF 보호 비활성화 (테스트 환경에서만 사용)
    
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(customUserDetailsService)
//        Spring Security가 인증할 때 사용자 정보를 customUserDetailsService를 통해 가져오도록 설정
            .passwordEncoder(passwordEncoder());
//        비밀번호 암호화를 추가로 설정
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//    	return NoOpPasswordEncoder.getInstance(); // 테스트 목적, 평문 비밀번호를 사용할 때 사용됨
        return new BCryptPasswordEncoder();
    }
}
