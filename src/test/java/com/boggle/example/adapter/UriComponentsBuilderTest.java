package com.boggle.example.adapter;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.util.UriComponentsBuilder;

public class UriComponentsBuilderTest {

	@Test
	public void testBuild() {
        URI uri = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey", "{key}")
                .queryParam("QueryType", "Title")
                .queryParam("query", "{bookTitle}")
                .queryParam("output", "js")
                .build("ttbmay-primavera1830001", "파친코");
        
        System.out.println("uri: " + uri);
        
        Assertions.assertEquals("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?TTBkey=ttbmay-primavera1830001&QueryType=Title&query=파친코&output=js", uri.toString());
	}
	
	@Test
	public void testEncoding() {
		URI firstUri = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey", "ttbmay-primavera1830001")
                .queryParam("QueryType", "Title")
                .queryParam("query", "파친코")
                .queryParam("output", "js")
                .build()
                .toUri();
		
		System.out.println("firstUri: " + firstUri);
				
		URI secondUri = UriComponentsBuilder.fromUriString("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx")
                .queryParam("TTBKey", "ttbmay-primavera1830001")
                .queryParam("QueryType", "Title")
                .queryParam("query", "파친코")
                .queryParam("output", "js")
                .build(false).encode() 
                .toUri();
		
		System.out.println("secondUri: " + secondUri);
		
		Assertions.assertEquals("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?TTBkey=ttbmay-primavera1830001&QueryType=Title&query=파친코&output=js", firstUri.toString());
		Assertions.assertEquals("http://www.aladin.co.kr/ttb/api/ItemSearch.aspx?TTBkey=ttbmay-primavera1830001&QueryType=Title&query=파친코&output=js", secondUri.toString());
	}
}
