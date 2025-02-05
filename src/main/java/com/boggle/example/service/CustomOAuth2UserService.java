package com.boggle.example.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.boggle.example.dto.auth.OAuthAttributes;
import com.boggle.example.entity.UserEntity;
import com.boggle.example.entity.UserPrincipal;
import com.boggle.example.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final UserRepository userRepository;
	
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("CustomOAuth2UserService.loadUser()");
    	
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
    	OAuth2User oAuth2User = delegate.loadUser(userRequest);
    	
        // 사용자 정보 가져오기
//        String email = oAuth2User.getAttribute("email");
//        String name = oAuth2User.getAttribute("name");
    	
    	// 로그인 진행 중인 서비스를 구분
        // 네이버로 로그인 진행 중인지, 구글로 로그인 진행 중인지, ... 등을 구분
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
    	
        // OAuth2 로그인 진행 시 키가 되는 필드 값(Primary Key와 같은 의미)
        // 구글의 경우 기본적으로 코드를 지원
        // 하지만 네이버, 카카오 등은 기본적으로 지원 X
        String userNameAttributeName = userRequest.getClientRegistration() // 현재 OAuth2 요청과 연결된 클라이언트의 정보
                .getProviderDetails() // OAuth2 제공자(Provider)와 관련된 세부 정보
                .getUserInfoEndpoint() // 사용자의 프로필 정보(UserInfo)를 가져올 수 있는 엔드포인트에 대한 설정
                .getUserNameAttributeName(); // 사용자를 고유하게 식별하는 키(예: sub 또는 id)       

        // OAuth2UserService를 통해 가져온 OAuth2User의 attribute 등을 담을 클래스
        OAuthAttributes attributes = OAuthAttributes.of(
        		registrationId, 
        		userNameAttributeName, 
        		oAuth2User.getAttributes()
        	);
        System.out.println(attributes);
        
        // 사용자 저장 또는 업데이트
        UserEntity user = saveOrUpdate(attributes);
        
        // 세션에 사용자 정보 저장 (session에 저장하면 @AuthenticationPrincipal이 접근 불가능
//        httpSession.setAttribute("user", new SessionUser(user));
        
//        attributes.getAttributes().put("userId", user.getUserId());// Add userId to the attributes map
        Map<String, Object> modifiableAttributes = new HashMap<>(attributes.getAttributes());
        modifiableAttributes.put("userId", user.getUserId());
        
        // SecurityContext에 저장, 이후 애플리케이션 내에서 인증 객체로 사용
//        return new DefaultOAuth2User( 
//                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
////                attributes.getAttributes(),
//                modifiableAttributes,
//                attributes.getNameAttributeKey());
        
//        return new UserPrincipal(
//        		user,
//        		Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
//        		modifiableAttributes
//        	);
        
        return UserPrincipal.create(user, modifiableAttributes);
    }
    
    private UserEntity saveOrUpdate(OAuthAttributes attributes) {
        UserEntity user = userRepository.findByEmail(attributes.getEmail())
                // 구글 사용자 정보 업데이트(이미 가입된 사용자) => 업데이트
                .map(entity -> entity.update(
                		attributes.getName(), 
                		attributes.getPicture()
                	))
                // 가입되지 않은 사용자 => User 엔티티 생성
                .orElse(attributes.toEntity());

        return userRepository.save(user);
    }
}
