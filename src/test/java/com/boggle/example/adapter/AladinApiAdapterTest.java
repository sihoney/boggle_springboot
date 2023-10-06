package com.boggle.example.adapter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
//@Transactional
//@TestPropertySource(locations = "classpath:application-api.properties")
//@ActiveProfiles("api")
public class AladinApiAdapterTest {

	@Autowired
	AladinApiAdapter aladinApiAdapter;

	@Test
	public void escapingCheckTest() throws Exception {
		//Given
		String query = "페스트";
		int pageNumber = 1;
		int pageSize = 4;
//		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		
		//When
		while(pageNumber < 6) {
			Pageable pageable = PageRequest.of(pageNumber, pageSize);
			
			aladinApiAdapter.searchBooks(query, pageable);
			
			pageNumber++;
		}
		
		//Then
	}
	
//	@Test
//	public void getBookInfoTest() throws JsonMappingException, JsonProcessingException {
//		//Given
//		String bookTitle = "파친코";
//		
//		//When
//		List<BookEntity> response = aladinApiAdapter.getBookInfo(bookTitle);
//		
//		//Then
//		Assertions.assertNotNull(response);
//		Assertions.assertEquals(BookEntity.class, response.get(0).getClass());
//	}
	
//	@Test
//	public void searchBooksTest() {
//		//Given
//		String bookTitle = "파친코";
//		String crtPage = "2";
//		
//		//When
//		List<BookEntity> response = aladinApiAdapter.searchBooks(bookTitle, crtPage);
//		
//		//Then
//		Assertions.assertNotNull(response);
//		Assertions.assertEquals(BookEntity.class, response.get(0).getClass());
//	}
}
