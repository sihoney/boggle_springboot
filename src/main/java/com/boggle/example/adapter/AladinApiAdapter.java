package com.boggle.example.adapter;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.boggle.example.dto.ApiResponse;
import com.boggle.example.entity.BookEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AladinApiAdapter {

	@Value("${aladin.api_key}")
	private String key;

	private final RestTemplate restTemplate;
	private ObjectMapper objectMapper = JsonMapper.builder()
		    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
		    .build();

	public AladinApiAdapter(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public List<BookEntity> getBookInfo(String bookTitle) throws JsonMappingException, JsonProcessingException  {
		
        URI uri = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey", key)
                .queryParam("QueryType", "Title")
                .queryParam("query", bookTitle)
                .queryParam("output", "js")
                .build()
                .toUri();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);

        String responseBody = responseEntity.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
//        String modifiedString = responseBody.replace(";", "");
    
        ApiResponse wrapperClass;
		try {
			wrapperClass = objectMapper.readValue(jsonNode.toString(), ApiResponse.class);
			return wrapperClass.getItem();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			System.out.println(">>> JsonMappingException error");
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.out.println(">>> JsonProcessingException error");
			e.printStackTrace();
			return null;
		}
	}
	
	public ApiResponse searchBooks(String query, Pageable pageable) throws Exception {
		
        URI uri = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey", key)
                .queryParam("QueryType", "Title")
                .queryParam("query", query)
                .queryParam("output", "js")
                .queryParam("MaxResults", pageable.getPageSize())
                .queryParam("Start", pageable.getPageNumber())
                .build()
                .toUri();
        
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        
        String responseBody = responseEntity.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);

        ApiResponse wrapperClass;
		try {
			wrapperClass = objectMapper.readValue(jsonNode.toString(), ApiResponse.class);
			return wrapperClass;
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			System.out.println(">>> JsonMappingException error");
			e.printStackTrace();
			return null;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			System.out.println(">>> JsonProcessingException error");
			e.printStackTrace();
			return null;
		}
        
	}
}
