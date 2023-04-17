package com.dkbmc.ifcm.module.common.parse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.dkbmc.ifcm.dto.common.MetaWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

public class MetaParser<T> {
	// Module Meta List Key
	private ObjectMapper objectMapper;

	/**
	 *
	 * @param <T>
	 * @param meta_data : Read System File Data
	 * @param meta_class : DTO Class
	 * @return Meta Class List
	 * @throws JsonProcessingException
	 * @throws JsonMappingException
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> getMetaEntityEntries(StringBuilder meta_data, String meta_name) throws JsonMappingException, JsonProcessingException {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new SimpleModule().addDeserializer(MetaWrapper.class, new MetaDeserializer(meta_name)));

		MetaWrapper<T> metaData = objectMapper.readValue(meta_data.toString(), MetaWrapper.class);

		return metaData.getDataList();
	}

	/**
	 *
	 * @param data_list : Meta Class List
	 * @return Stringbuilder : Json format String
	 * @throws JsonProcessingException
	 *
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public StringBuilder setMetaEntityEntries(List<T> data_list) throws JsonProcessingException {
		objectMapper = new ObjectMapper();
		// java 1.8
		JavaTimeModule javaTimeModule = new JavaTimeModule();
		javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
		objectMapper.findAndRegisterModules();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		objectMapper.registerModule(new SimpleModule().addSerializer(MetaWrapper.class, new MetaSerializer()));

		MetaWrapper<T> metaData = new MetaWrapper();
		metaData.setDataList(data_list);

		return new StringBuilder().append(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(metaData));
	}
}
