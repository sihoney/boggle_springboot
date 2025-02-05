package com.boggle.example.dto.auth;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

public class CustomOAuth2User extends DefaultOAuth2User {
    private String userId;
    private String nickname;

    public CustomOAuth2User(
    		Collection<? extends GrantedAuthority> authorities, 
    		Map<String, Object> attributes, 
    		String nameAttributeKey, 
    		String userId
    	) {
        super(authorities, attributes, nameAttributeKey);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}