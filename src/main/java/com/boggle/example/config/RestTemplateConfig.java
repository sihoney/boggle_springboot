package com.boggle.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
//    	RestTemplate의 ClientHttpRequestFactory 구현체 설정
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

//		커넥션 객체를 생성하는 데 소요되는 최대시간 설정        
        factory.setConnectTimeout(3000);
//        데이터 처리를 요청하고 응답받기까지 소요되는 최대 시간 
        factory.setReadTimeout(3000);
//        요청 메시지의 바디를 버퍼링하는 기능을 제공(기본 설정은 true)
//        +) 용량이 큰 파일을 전송한다면 버퍼에 저장되므로 false로 설정하는 것이 좋다.
        factory.setBufferRequestBody(false);

        return factory;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory clientHttpRequestFactory) {
//		clientHttpRequestFactory를 인자로 받는 생성자를 사용하여 RestTemplate 객체를 생성
    	RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);

//        restTemplate.getInterceptors().add(new IdentityHeaderInterceptor());
//        ResponseErrorHandler 인터페이스 기본 구현체
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

//        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//        restTemplate.getMessageConverters().add(new ApiResponseConverter());
        
        return restTemplate;
    }
}
