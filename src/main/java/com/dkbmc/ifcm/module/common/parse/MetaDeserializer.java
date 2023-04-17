package com.dkbmc.ifcm.module.common.parse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import com.dkbmc.ifcm.dto.common.MetaWrapper;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

public class MetaDeserializer<T> extends JsonDeserializer<MetaWrapper<T>> {

	private String entityName;

	public MetaDeserializer(String entity_name) {
		this.entityName = entity_name;
	}

	@Override
    public MetaWrapper<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        try {
            JsonNode rootNode = p.getCodec().readTree(p);
            //String entityName = rootNode.get("entity_name").asText();
            //Class<T[]> parametricListType = (Class<T[]>) Class.forName("[Lcom.mod.dto." + entityName + ";");

            JsonNode dataNode = rootNode.get(ParseConst.META_LIST_KEY);
            ObjectMapper objectMapper = new ObjectMapper();

            // java 1.8
            JavaTimeModule javaTimeModule=new JavaTimeModule();
            javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
            objectMapper.findAndRegisterModules();
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

            JavaType parametricListType = ctxt.getTypeFactory().constructParametricType(List.class, Class.forName(this.entityName));
            List<T> dataList = objectMapper.readValue(dataNode.toString(), parametricListType);

            return new MetaWrapper<>(entityName, dataList);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}