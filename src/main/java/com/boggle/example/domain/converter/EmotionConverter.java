package com.boggle.example.domain.converter;

import java.util.Objects;

import javax.persistence.AttributeConverter;

import org.springframework.stereotype.Component;

import com.boggle.example.util.EmotionEnum;

@Component
public class EmotionConverter implements AttributeConverter<EmotionEnum, Integer>{

	@Override
	public Integer convertToDatabaseColumn(EmotionEnum attribute) {
		if(Objects.isNull(attribute))
			return null;
		
		return attribute.getValue();
	}
	
	@Override
	public EmotionEnum convertToEntityAttribute(Integer dbData) {
		if(Objects.isNull(dbData))
			return null;
		
		return EmotionEnum.fromValue(dbData);
	}
}
