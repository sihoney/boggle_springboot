package com.boggle.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Entity(name = "font")
@Table(name = "font")
public class FontEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "font_id")
	private Long fontId;
	
	@Column(name = "font_name")
	private String fontName;
}
