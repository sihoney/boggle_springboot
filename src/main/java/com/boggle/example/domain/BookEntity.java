package com.boggle.example.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(name = "book")
@Table(name = "book")
public class BookEntity {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("isbn13")
	@Column(name = "isbn")
	private Long isbn;
	
	@JsonProperty("title")
	@Column(name = "book_name")
	private String bookName;
	
	@Column(name = "author")
	private String author;
	
	@JsonProperty("categoryId")
	@Column(name = "genre_id")
	private Long genreId;
	
//	@JsonProperty("categoryId")
//	@ManyToOne
//	private GenreEntity genreEntity;
	
	@JsonProperty("link")
	@Column(name = "book_url")
	private String bookUrl;
	
	@JsonProperty("cover")
	@Column(name = "cover_url")
	private String coverUrl;
	
//	@OneToMany(mappedBy = "bookEntity")
//	List<ReviewEntity> reviewList;
	
	private BookEntity(Long isbn, String bookName, String author, Long genreId, String bookUrl, String coverUrl) {
		this.isbn = isbn;
		this.bookName = bookName;
		this.author = author;
		this.genreId = genreId;
		this.bookUrl = bookUrl;
		this.coverUrl = coverUrl;
	}
	
	public static BookEntity of(Long isbn, String bookName, String author, Long genreId,
			String bookUrl, String coverUrl) {
		return new BookEntity(isbn, bookName, author, genreId, bookUrl, coverUrl);
	}
}
