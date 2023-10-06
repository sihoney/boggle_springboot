package com.boggle.example.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SpecialCharacterCheck {
	
	public static String characterCheck(String input) {
//		System.out.println(input);
		
		if(input.length() > 2)
			input = input.substring(1, input.length() - 1);
		
//      String pattern = "[!@#$%^&*(),.?\":{}|<>]";
		String pattern = "[']";
//		String pattern = "[!@#$%^&*(),.?\"':{}|<>\\-]";
        Pattern regex = Pattern.compile(pattern);
        Matcher matcher = regex.matcher(input);
//        System.out.println(matcher.find());
        
        if(matcher.find()) {
        	System.out.println(input);
        	
        	String matchString = matcher.group();
        	int startIndex = matcher.start();
        	int endIndex = matcher.end();
        	
//        	if(startIndex > 0 && endIndex < input.length() && !(input.charAt(startIndex - 1) == '\\')) {
//        		input = matcher.replaceAll('\\' + matchString);
//        		System.out.println(input);
//        	} 
        }
        
        return input;
	}
	
    public static String escapingCheck(String jsonString) throws Exception {
        //String jsonString = "{ \"title\" : \"마케팅 추월자 - 경쟁 없이 빠르게 팔리는 패스트 마케팅 7단계 공략집\" }";

    	System.out.println("escapingCheck()");
    	
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonString);
        JsonNode itemJsonNode = jsonNode.get("item");
        
        if (itemJsonNode.isArray()) {
            List<JsonNode> jsonNodeList = new ArrayList<>();
            itemJsonNode.forEach(jsonNodeList::add);

            for (JsonNode node : jsonNodeList) {
//            	System.out.println(node.get("title"));
            	Iterator<String> iterator = node.fieldNames();

            	while(iterator.hasNext()) {
            		characterCheck( node.get(iterator.next()).toString() );
            	}
            }
        }
        
		return jsonString;
    }
}
