package com.boggle.example.dto.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.boggle.example.entity.UserEntity;

public class CustomUserDetails implements UserDetails {
	
	private UserEntity userEntity;
	private List<GrantedAuthority> authorities;
	
	public CustomUserDetails(UserEntity userEntity, List<GrantedAuthority> authorities) {
		this.userEntity = userEntity;
		this.authorities = authorities;
	}
	
	public static CustomUserDetails create(UserEntity userEntity) {
		return new CustomUserDetails(
				userEntity,
				List.of(() -> "ROLE_USER")
				);
	}
	
	public Long getUserId() {
		return userEntity.getUserId();
	}
	
	public String getUserProfile() {
		return userEntity.getUserProfile();
	}
	
	public String getNickname() {
		return userEntity.getNickname();
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userEntity.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userEntity.getEmail();
	}
}
