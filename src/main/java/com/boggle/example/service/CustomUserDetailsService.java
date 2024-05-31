package com.boggle.example.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.boggle.example.domain.UserEntity;
import com.boggle.example.dto.CustomUserDetails;
import com.boggle.example.repository.UserRepository;

@Service
public class CustomUserDetailsService implements  UserDetailsService {
 
	@Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	
    	UserEntity user = userRepository.findByEmail(email)
    			 .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));

//    	System.out.println(user);
    	
    	Collection<? extends GrantedAuthority> authorities = getAuthorities("ROLE_USER");
    	
        return new CustomUserDetails(
        		email,  // username
        		user.getPassword(), // password
        		user.getUserId(), // userId
        		user.getNickname(),
        		user.getUserProfile(),
        		authorities);    	
    	
//        User result = new org.springframework.security.core.userdetails.User(
//            email,
//            user.getPassword(),
//            true, true, true, true,
//            getAuthorities("ROLE_USER"));        
//        return result;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
