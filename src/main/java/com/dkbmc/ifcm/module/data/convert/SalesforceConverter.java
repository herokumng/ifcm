package com.dkbmc.ifcm.module.data.convert;

import java.util.Map;

import com.dkbmc.ifcm.dto.mapping.MAP1102;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.moduleType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.salesforceType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.systemType;

public class SalesforceConverter extends CommonConverter {

	private Map<salesforceType, moduleType> salesforce;

	public SalesforceConverter(DataTypeMapper data_type_mapper, String date_patterns) {
		super(data_type_mapper, date_patterns);
		dataTypeMapper.setDataTypeMapper(systemType.SALESFORCE);
		salesforce = dataTypeMapper.getSalesforce();
	}

	@Override
	public Object conversionModuleToSystem(MAP1102 field_mapping_info, Object field_data) {
		Object moduleValue = null;

//		String sourceFieldType = field_mapping_info.getSrc_fld_type();
//		salesforceType fieldType = salesforceType.valueOf(sourceFieldType);
		String sourceFieldType = field_mapping_info.getTgt_fld_type().toUpperCase();
		salesforceType fieldType = salesforceType.valueOf(sourceFieldType);
		moduleType dataType = salesforce.get(fieldType);

		if (dataType.equals(moduleType.STRING)) {
			switch (fieldType) {
//			case ADDRESS:
//			case LOCATION:
			case BOOLEAN :
				moduleValue = conversionIntStringToBoolean(String.valueOf(field_data));
				break;
			case DATE :
				moduleValue = conversionToDate(String.valueOf(field_data));
				break;
			case DATETIME :
				moduleValue = conversionToDatetime(String.valueOf(field_data));
				break;
			case TIME :
				moduleValue = conversionToTime(String.valueOf(field_data));
				break;
			default:
				moduleValue = String.valueOf(field_data);
			}
		}

		if (dataType.equals(moduleType.NUMBER)) {
			switch (fieldType) {
			case BYTE, INT :
				moduleValue = Integer.valueOf(String.valueOf(field_data));
//			case CURRENCY :
//			case PERCENT :
			default :
				moduleValue = field_data;
			}
		}

		if (dataType.equals(moduleType.OBJECT)) {
			moduleValue = field_data;
		}

		return moduleValue;
	}

	@Override
	public Object conversionSystemToModule(MAP1102 field_mapping_info, Object field_data) {
		Object targetValue = null;
		String targetFieldType = field_mapping_info.getTgt_fld_type().toUpperCase();
		salesforceType fieldType = salesforceType.valueOf(targetFieldType);
		moduleType dataType = salesforce.get(fieldType);

		return targetValue;
	}

}
