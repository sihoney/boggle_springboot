package com.boggle.example.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;

@Getter
public class CustomUserDetails implements UserDetails {

	private String profileUrl;
	private String nickname;
    private Long userId; // 추가 정보: 이메일
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String username, String password, Long userId, String nickname, String profileUrl, Collection<? extends GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.userId = userId;//        this.fullName = fullName;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        // 권한 설정 등
    }  
    
    // 기타 UserDetails 메서드 오버라이드
    @Override
    public boolean isAccountNonLocked() {
        return true; // 또는 계정이 잠겨 있는지 여부에 따른 로직을 구현
    }

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}
