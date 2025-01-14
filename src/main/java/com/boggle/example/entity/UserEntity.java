package com.boggle.example.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity(name = "users")
@Table(name = "users")
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	
	public static UserEntity of(String userName, String nickname, String email, String password, String userProfile) {
		UserEntity userEntity = new UserEntity();
		
		userEntity.userName = userName;
		userEntity.nickname = nickname;
		userEntity.email = email;
		userEntity.password = password;
		userEntity.userProfile = userProfile;
		
		return userEntity;
	}
}
