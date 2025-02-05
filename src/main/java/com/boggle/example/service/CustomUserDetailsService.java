package com.boggle.example.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.boggle.example.dto.auth.CustomUserDetails;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
 
	@Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
    	UserEntity user = userRepository.findByEmail(email)
    			 .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

    	user.setUserProfile("images/user_profile/" + user.getUserProfile());
    	
//    	Collection<? extends GrantedAuthority> authorities = getAuthorities("ROLE_USER");
    	
    	return UserPrincipal.create(user);
//    	return CustomUserDetails.create(user);
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
