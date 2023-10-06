package com.boggle.example.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonValue;

public enum EmotionEnum {

	ANGER("분노", 0),
	SADNESS("슬픔", 1),
	PAIN("고통", 2),
	UNREST("불안", 3),
	SHAME("창피", 4),
	JOY("기쁨", 5),
	LOVE("사랑", 6),
	HOPE("바람", 7);
	
	private final String param;
	private final Integer value;
	
//	Arrays.stream(Emotion.values()): 상수 배열을 스트림으로 변환
//	Emotion::getValue: 키 매핑 함수, 상수의 숫자 값을 키로 설정
//	Function.identity(): 값 매핑 함수, 스트림의 각 요소를 그대로 값으로 설정
	private static final Map<String, EmotionEnum> paramMap = Arrays.stream(EmotionEnum.values())
			.collect(Collectors.toMap(
					EmotionEnum::getParam, 
					Function.identity()
			));
	private static final Map<Integer, EmotionEnum> valueMap = Arrays.stream(EmotionEnum.values())
			.collect(Collectors.toMap(
					EmotionEnum::getValue, 
					Function.identity()
			));
	
	EmotionEnum(String param, Integer value) {
		this.param = param;
		this.value = value;
	}
	
//	formParam, fromValue: Enum 상수를 찾아주는 메서드
	public static EmotionEnum fromParam(String param) {
		return Optional.ofNullable(param)
				.map(paramMap::get)
				.orElseThrow(() -> new IllegalArgumentException("param is not valid"));
	}
	
	public static EmotionEnum fromValue(Integer value) {
		return Optional.ofNullable(value)
				.map(valueMap::get)
				.orElseThrow(() -> new IllegalArgumentException("value is not valid"));
	}
	
	public static List<String> getList() {
        List<String> koreanNames = new ArrayList<>();

        // EmotionEnum Enum에서 한글 부분만 추출
        for (EmotionEnum emotion : EmotionEnum.values()) {
            koreanNames.add(emotion.getParam());
        }
        
        return koreanNames;
	}

	
//	Enum -> JSON) 직렬화할 때 지정된 메서드 호출
	@JsonValue
	public String getParam() {
		return this.param;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
