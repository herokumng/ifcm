package com.dkbmc.ifcm.module.common.parse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

import com.dkbmc.ifcm.dto.common.MetaWrapper;
import com.dkbmc.ifcm.module.common.utils.MetaUtils;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;


public class MetaSerializer<T> extends JsonSerializer<MetaWrapper<T>> {
	private MetaUtils metaUtil;

	public MetaSerializer() {
		metaUtil = new MetaUtils();
	}

	@Override
	public void serialize(MetaWrapper<T> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		// TODO Auto-generated method stub

		try {
			if (value != null && value.getDataList().size() > 0) {
				Iterator<T> iterator = value.getDataList().iterator();
				ObjectMapper objectMapper = new ObjectMapper(); //convertValue 사용 시 필요.
				// java 1.8
				JavaTimeModule javaTimeModule = new JavaTimeModule();
				javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
				objectMapper.findAndRegisterModules();
				objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
				Map<String, Object> map;

				gen.writeStartObject();
				gen.writeFieldName(ParseConst.META_LIST_KEY);
				gen.writeStartArray();

				// VO에서 필드 정렬 : @JsonPropertyOrder({"name", "email"}) 사용

				while (iterator.hasNext()) {
					gen.writeStartObject();
//					 map = objectMapper.convertValue(iterator.next(), Map.class);
					map = metaUtil.covertEntityToMap(iterator.next());
					for (String key : map.keySet()) {
						gen.writeFieldName(key);
						gen.writeObject(map.get(key));
					}
					gen.writeEndObject();
				}

				gen.writeEndArray();
				gen.writeEndObject();
				gen.close();

			} else {
				gen.writeStartObject();
				gen.writeFieldName(ParseConst.META_LIST_KEY);
				gen.writeStartArray();
				gen.writeEndArray();
				gen.writeEndObject();
				gen.close();

				System.out.println("empty data list");
			}
		} catch (IllegalArgumentException | IllegalAccessException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
