package com.boggle.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class WebClientConfig {
	  
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
            .defaultHeader(
        		HttpHeaders.CONTENT_TYPE, 
        		MediaType.APPLICATION_JSON_VALUE
        	).codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024)) // 2MB
            .build();
    }
    
    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER) // 백슬래시 이스케이프 허용
            .addModule(new JavaTimeModule()) // LocalDateTime 직렬화 지원 추가
            .build();
    }
}
