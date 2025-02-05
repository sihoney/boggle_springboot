package com.boggle.example.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserPrincipal implements OAuth2User, UserDetails {

	private UserEntity userEntity;
	private List<GrantedAuthority> authorities;
	private Map<String, Object> oauthUserAttributes;
	
	public UserPrincipal(
			UserEntity userEntity, 
			List<GrantedAuthority> authorities, 
			Map<String, Object> oauthUserAttributes
			) {
		this.userEntity = userEntity;
		this.authorities = authorities;
		this.oauthUserAttributes = oauthUserAttributes;
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
	
    /**
     * OAuth2 로그인시 사용
     */
    public static UserPrincipal create(UserEntity user, Map<String, Object> oauthUserAttributes) {
        return new UserPrincipal(
        		user, 
        		List.of(() -> "ROLE_USER"), 
        		oauthUserAttributes
        	);
    }

    public static UserPrincipal create(UserEntity user) {
        return new UserPrincipal(
        		user, 
        		List.of(() -> "ROLE_USER"), 
        		new HashMap<>()
        	);
    }
	
	@Override
	public Map<String, Object> getAttributes() {
		// TODO Auto-generated method stub
		return Collections.unmodifiableMap(oauthUserAttributes);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return Collections.unmodifiableList(authorities);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return String.valueOf(userEntity.getUserName());
	}
	
	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userEntity.getEmail();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return userEntity.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
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
