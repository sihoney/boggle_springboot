package com.boggle.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import com.boggle.example.service.CustomOAuth2UserService;
import com.boggle.example.service.CustomUserDetailsService;

@Configuration // 설정을 정의하는 클래스
@EnableWebSecurity //Spring Security 활성화
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;
//    private UserDetailsService userDetailsService;
	
    @Bean
    public SecurityFilterChain securityFilterChanin(HttpSecurity http) throws Exception {
        http
        .authorizeRequests(auth -> auth
            .antMatchers("/boggle", "/reviews-with-style", "/users/**", "/success", "/failure").permitAll()
            .antMatchers("/css/**", "/js/**", "/images/**", "/bootstrap/**").permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
            .loginPage("/users/login")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/boggle") // Redirect based on role or preferences
            .successHandler((request, response, authentication) -> {
                String targetUrl = authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))
                        ? "/admin/dashboard" : "/boggle";
                response.sendRedirect(targetUrl);
            })
            .failureUrl("/users/login?result=failure")
            .permitAll()
        )
        .logout(logout -> logout
                .logoutUrl("/users/logout") // 로그아웃 요청 URL
                .logoutSuccessUrl("/users/login?logout") // 로그아웃 성공 후 이동할 URL
                .deleteCookies("JSESSIONID") // 쿠키 삭제
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 제거
         )
        .sessionManagement(session -> session
            .sessionFixation().migrateSession()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .invalidSessionUrl("/users/login?message=Session expired. Please log in again.")
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
        )
        .exceptionHandling(exceptions -> exceptions
            .accessDeniedPage("/access-denied")
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        )
        .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (테스트 환경에서만 사용)
        .oauth2Login() // OAuth 2.0 로그인 활성화
	        .loginPage("/users/login")
	        .defaultSuccessUrl("/boggle", true)
	        .userInfoEndpoint()
	            .userService(customOAuth2UserService); // 사용자 정보 처리 커스터마이징 
	            
        return http.build();
    }
    
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
//        return new CustomOAuth2UserService();
//    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, CustomUserDetailsService customUserDetailsService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
            .userDetailsService(customUserDetailsService)
            .passwordEncoder(passwordEncoder)
            .and()
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
//    	return NoOpPasswordEncoder.getInstance(); // 테스트 목적, 평문 비밀번호를 사용할 때 사용됨
        return new BCryptPasswordEncoder();
    }
}
