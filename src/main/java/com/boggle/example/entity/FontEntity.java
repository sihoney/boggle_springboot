package com.boggle.example.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "font")
@Table(name = "font")
public class FontEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "font_id")
	private Long fontId;
	
	@Column(name = "font_name")
	private String fontName;
	
	private FontEntity (Long fontId) {
		this.fontId = fontId;
	}
	
	public static FontEntity of(Long fontId) {
		return new FontEntity(fontId);
	}
}
