package com.boggle.example.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "user_name")
	private String userName;
	
	@Column(name = "nickname", unique=true)
	private String nickname;
	
	@Column(name = "email", unique=true)
	private String email;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "user_profile")
	private String userProfile;
	
	@Column(name = "created_at")
	private LocalDate joinDate;
	
    @Builder
    public UserEntity(String userName, String email, 
    		String userProfile, String nickname,
    		Long userId, String password) {
        this.userName = userName;
        this.email = email;
        this.userProfile = userProfile;
        this.nickname = nickname;
        this.userId = userId;
        this.password = password;
    }
	
	public static UserEntity of(String userName, String nickname, String email, String password, String userProfile) {
		UserEntity userEntity = new UserEntity();
		
		userEntity.userName = userName;
		userEntity.nickname = nickname;
		userEntity.email = email;
		userEntity.password = password;
		userEntity.userProfile = userProfile;
		
		return userEntity;
	}
	
    public UserEntity update(String name, String picture) {
        this.userName = name;
        this.userProfile = picture;

        return this;
    }

//    public String getRoleKey() {
//        return this.role.getKey();
//    }
}
