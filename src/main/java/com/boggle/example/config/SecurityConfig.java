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

@Configuration //해당 클래스가 설정을 정의하는 클래스
@EnableWebSecurity //Spring Security를 사용하여 웹 보안을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
//    private UserDetailsService userDetailsService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests() //요청에 대한 접근 권한을 설정하는 메서드
                .antMatchers("/**/mybook").authenticated() //홈 페이지와 루트 페이지에 대한 접근을 모든 사용자에게 허용
//                .antMatchers("/admin/**").hasRole("ADMIN") // ADMIN 권한이 있는 사용자에게만 허용
                .anyRequest().permitAll() //나머지 모든 요청에 대해 사용자 인증이 필요하다는 것을 의미                
                .and()
            .formLogin() //폼 기반 로그인을 구성하는 메서드
                .loginPage("/user/loginForm") //사용자 정의 로그인 페이지의 경로를 설정
                .loginProcessingUrl("/custom-login") // 로그인 처리 URL 설정 (선택 사항)
                .defaultSuccessUrl("/boggle", true) // 로그인 성공 시 리다이렉트할 URL
                .failureUrl("/user/loginForm?error=true") // 로그인 실패 시 리다이렉트할 URL
                .permitAll()
                .and()    
            .logout() //로그아웃을 처리하는 메서드
            	.logoutUrl("/logout")
	            .logoutSuccessUrl("/user/loginForm")
                .permitAll() //로그아웃에 모든 사용자에게 접근 권한을 부여
                .and()
            .sessionManagement()
                .sessionFixation().migrateSession() // 세션 고정 보호 전략 설정
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 필요한 경우에만 세션 생성
                .invalidSessionUrl("/user/loginForm") // 세션이 만료된 경우 리다이렉트할 URL
                .maximumSessions(1) // 최대 동시 세션 수
                .maxSessionsPreventsLogin(true) // 새로운 로그인 시 이전 세션 만료 여부
                .and()
            .and()
            .exceptionHandling()
                .accessDeniedPage("/access-denied") // 권한이 없는 사용자가 접근할 때 리다이렉트할 페이지 설정
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)) // 인증되지 않은 사용자가 접근할 때의 처리
                .and()
            .csrf().disable(); // CSRF 보호 비활성화 (테스트 환경에서만 사용); 
    
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .userDetailsService(customUserDetailsService);
//            .passwordEncoder(passwordEncoder())
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//    	return NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }
}
