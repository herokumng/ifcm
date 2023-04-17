package com.dkbmc.ifcm.module.data.convert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dkbmc.ifcm.dto.mapping.MAP1102;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.systemType;

/**
 *
 * @WhenToUse : Data Upload & Upsert Target Object
 * @Processing : Source Field(Data) Type Data -> java Type Data -> Source/Target
 *             Field(Data) Type Data Convert to Record Units(Data line)
 * @author DY
 * @param <T>
 *
 */
public class DataConverter {
	private DataTypeMapper dataTypeMapper;
	private String sourceSystem, targetSystem;
	private List<MAP1102> fieldMappingInfo;
	private String datePatterns;
	// Add Converter
	private SalesforceConverter salesforceConverter;
	private OracleConverter oracleConverter;

	// 현재 DB 접속 모듈이 따로 없음으로 기본 값은 오라클DB Data Type 사용으로 상정.
	public DataConverter(String source_system, String target_system, List<MAP1102> field_mapping_info,
			String datetime_pattern) {
		if (source_system.toUpperCase().equals(systemType.DEFAULT.name())) {
			sourceSystem = systemType.ORACLE.name();
		} else {
			sourceSystem = source_system.toUpperCase();
		}

		if (target_system.toUpperCase().equals(systemType.DEFAULT.name())) {
			targetSystem = systemType.ORACLE.name();
		} else {
			targetSystem = target_system.toUpperCase();
		}

		fieldMappingInfo = field_mapping_info;
		datePatterns = datetime_pattern;
		dataTypeMapper = new DataTypeMapper();
		initAnyConverter();
	}

	private void initAnyConverter() {
		salesforceConverter = null;
		oracleConverter = null;
	}

	public Map<String, Object> convertRecord(Map<String, String> field_data_map)
			throws IllegalArgumentException, SQLException, IOException {
		Map<String, Object> conversionRecordMap = new HashMap<>();

		for (String header : field_data_map.keySet()) {
			for (MAP1102 fieldMappingInfo : fieldMappingInfo) {
				if (header.equals(fieldMappingInfo.getSrc_fld_name())) {
					conversionRecordMap.put(fieldMappingInfo.getTgt_fld_name(),
							convertItem(fieldMappingInfo, field_data_map.get(header)));
				}
			}
		}
		return (conversionRecordMap == null ? null : conversionRecordMap);
	}

	private Object convertItem(MAP1102 field_mapping_info, String field_data)
			throws IllegalArgumentException, SQLException, IOException {
		Object conversionItem = null;
		conversionItem = convertToModule(field_mapping_info, field_data);
		conversionItem = convertToTarget(field_mapping_info, conversionItem);
		return (conversionItem == null ? null : conversionItem);
	}

	private Object convertToModule(MAP1102 field_mapping_info, Object field_data)
			throws IllegalArgumentException, SQLException, IOException {
		Object moduleData = null;

		if (sourceSystem.equals(systemType.SALESFORCE.name())) {
			isExistAnyConverter(salesforceConverter, systemType.SALESFORCE);
			moduleData = salesforceConverter.conversionSystemToModule(field_mapping_info, field_data);
		}

		if (sourceSystem.equals(systemType.ORACLE.name())) {
			isExistAnyConverter(oracleConverter, systemType.ORACLE);
			moduleData = oracleConverter.conversionSystemToModule(field_mapping_info, field_data);
		}

		return moduleData;
	}

	private Object convertToTarget(MAP1102 field_mapping_info, Object module_data) throws SQLException, IOException {
		Object targetData = null;

		if (targetSystem.equals(systemType.SALESFORCE.name())) {
			isExistAnyConverter(salesforceConverter, systemType.SALESFORCE);
			targetData = salesforceConverter.conversionModuleToSystem(field_mapping_info, module_data);
		}

		if (targetSystem.equals(systemType.ORACLE.name())) {
			isExistAnyConverter(oracleConverter, systemType.ORACLE);
			targetData = oracleConverter.conversionModuleToSystem(field_mapping_info, module_data);
		}

		return targetData;
	}

	private void isExistAnyConverter(Object any_converter, systemType system_type) {
		if (any_converter == null) {
			if (system_type.equals(systemType.SALESFORCE)) {
				salesforceConverter = new SalesforceConverter(dataTypeMapper, datePatterns);
			}
			if (system_type.equals(systemType.ORACLE)) {
				oracleConverter = new OracleConverter(dataTypeMapper, datePatterns);
			}
		}
	}
}
