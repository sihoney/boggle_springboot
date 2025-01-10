package com.boggle.example.domain;

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
@Entity(name = "genre")
@Table(name = "genre")
public class GenreEntity {
	
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "genre_id")
	private Long genreId;
	
	@Column(name = "genre_name")
	private String genreName;
	
//	private Long totalCount;
	
	private GenreEntity(String name, Long genreId) {
		this.genreName = name;
		this.genreId = genreId;
	}
	
//	private GenreEntity(String name, Long genreId, Long totalCount) {
//		this.genreName = name;
//		this.genreId = genreId;
//		this.totalCount = totalCount;
//	}
	
	public static GenreEntity of(String name, Long genreId) {
		return new GenreEntity(name, genreId);
	}
	
//	public static GenreEntity of(String name, Long genreId, Long totalCount) {
//		return new GenreEntity(name, genreId, totalCount);
//	}
}
