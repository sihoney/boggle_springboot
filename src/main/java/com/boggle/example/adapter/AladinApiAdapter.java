package com.boggle.example.adapter;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.boggle.example.dto.ApiResponse;
import com.boggle.example.dto.BookDTO;
import com.boggle.example.dto.BookSearchRequest;
import com.boggle.example.exception.AladinApiException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AladinApiAdapter {
	
   private static final String QUERY_TYPE_TITLE = "Title";
   private static final String OUTPUT_FORMAT = "js";
	
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
//    private final RestTemplate restTemplate;
//	private ObjectMapper objectMapper = JsonMapper.builder()
//		    .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER)
//		    .build();
	
	@Value("${aladin.api.key}")
	private String apiKey;
	
	@Value("${aladin.api.url}")
    private String apiUrl;

    /**
     * 도서 제목으로 도서 정보 검색
     */
	public List<BookDTO> getBookInfo(String bookTitle) throws JsonMappingException, JsonProcessingException  {
		try {
            String response = callAladinApi(buildUri(BookSearchRequest.builder()
                    .query(bookTitle)
                    .build()));

            ApiResponse apiResponse = parseResponse(response);
//            return apiResponse.getItem();
            return (List<BookDTO>) apiResponse.getItem();
            
		} catch (Exception e) {
			log.error("Failed to get book info for title: {}", bookTitle, e);
            throw new AladinApiException("도서 정보 조회 중 오류가 발생했습니다.", e);
		}
	}
	
    /**
     * 페이징이 포함된 도서 검색
     */
    public ApiResponse searchBooks(String query, Pageable pageable) {
        try {
            String response = callAladinApi(buildUri(BookSearchRequest.builder()
                .query(query)
                .maxResults(pageable.getPageSize())
                .start(pageable.getPageNumber())
                .build()));

            return parseResponse(response);
            
        } catch (Exception e) {
            log.error("Failed to search books with query: {}, page: {}", query, pageable.getPageNumber(), e);
            throw new AladinApiException("도서 검색 중 오류가 발생했습니다.", e);
        }
    }
    
    private String callAladinApi(URI uri) {
        return webClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    
    private URI buildUri(BookSearchRequest request) {
        return UriComponentsBuilder.fromUriString(apiUrl)
            .queryParam("TTBKey", apiKey)
            .queryParam("QueryType", QUERY_TYPE_TITLE)
            .queryParam("query", request.getQuery())
            .queryParam("output", OUTPUT_FORMAT)
            .queryParamIfPresent("MaxResults", Optional.ofNullable(request.getMaxResults()))
            .queryParamIfPresent("Start", Optional.ofNullable(request.getStart()))
            .build()
            .toUri();
    }
    
    private ApiResponse parseResponse(String response) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(response);
        return objectMapper.treeToValue(jsonNode, ApiResponse.class);
    }
}
