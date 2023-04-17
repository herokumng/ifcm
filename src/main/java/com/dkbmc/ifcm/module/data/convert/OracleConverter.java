package com.dkbmc.ifcm.module.data.convert;

import java.io.IOException;
import java.io.Reader;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.dkbmc.ifcm.dto.mapping.MAP1102;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.moduleType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.oracleType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.systemType;

public class OracleConverter extends CommonConverter {
	private Map<oracleType, moduleType> oracle;

	public OracleConverter(DataTypeMapper data_type_mapper, String date_patterns) {
		super(data_type_mapper, date_patterns);
		dataTypeMapper.setDataTypeMapper(systemType.ORACLE);
		oracle = dataTypeMapper.getOracle();
	}

	// source to module
	@Override
	public Object conversionSystemToModule(MAP1102 field_mapping_info, Object field_data)
			throws IllegalArgumentException, SQLException, IOException {
		Object moduleValue = null;

		String sourceFieldType = field_mapping_info.getSrc_fld_type().toUpperCase();
		oracleType fieldType = oracleType.valueOf(sourceFieldType);
		moduleType dataType = oracle.get(fieldType);

		if (dataType.equals(moduleType.STRING)) {
			switch (fieldType) {
			case CLOB, NCLOB:
				moduleValue = clobToString((Clob) field_data);
				break;
			default:
				moduleValue = String.valueOf(field_data);
			}
		}

		if (dataType.equals(moduleType.NUMBER)) {
			String numberString = String.valueOf(field_data);
			int precision = field_mapping_info.getSrc_fld_precision();
			int digits = field_mapping_info.getSrc_fld_digits();

			switch (fieldType) {
			case NUMBER :
				int type = getNumberType(field_mapping_info, precision, digits);
				if(type == ModuleConst.MODULE_DIGIT_1) moduleValue = conversionToBigInteger(numberString);
				if(type == ModuleConst.MODULE_DIGIT_3) moduleValue = conversionToLong(numberString);
				if(type == ModuleConst.MODULE_DIGIT_2) moduleValue = conversionToBigDecimal(numberString);
				if(type == ModuleConst.MODULE_DIGIT_4) moduleValue = conversionToDouble(numberString);
				break;
			default:
				moduleValue = conversionToDouble(numberString);
			}
		}

		// Oracle BFILE Type의 경우 Oracle JDBC Library 필요
		// 향후 DB 사용 시 추가 개발 필요, 파일 업로드 데이터가 Bㅑnary 일 가능성이 적음.
		if (dataType.equals(moduleType.BYTE_ARRAY)) {
			switch (fieldType) {
			case BLOB :
				moduleValue = new String(blobToByteArray((Blob) field_data));
				break;
//			case BFILE :break;
			default:
				moduleValue = null;
			}
		}
		return moduleValue;
	}

	// module to Target
	@Override
	public Object conversionModuleToSystem(MAP1102 field_mapping_info, Object field_data)
			throws SQLException, IOException {
		Object moduleValue = null;

		String sourceFieldType = field_mapping_info.getSrc_fld_type();
		oracleType fieldType = oracleType.valueOf(sourceFieldType);
		moduleType dataType = oracle.get(fieldType);

		if (dataType.equals(moduleType.STRING)) {
			switch (fieldType) {
			case CLOB :
				break;
			case NCLOB :
				break;
			case DATE :
				break;
			case TIMESTAMP :
				break;
			default:

			}
		}

		if (dataType.equals(moduleType.NUMBER)) {
			switch (fieldType) {
			case FLOAT :

				break;
			default :

			}
		}

		// Oracle BFILE Type의 경우 Oracle JDBC Library 필요
		// 향후 DB 사용 시 추가 개발 필요, 파일 업로드 데이터가 Bㅑnary 일 가능성이 적음.
		if (dataType.equals(moduleType.BYTE_ARRAY)) {
			switch (fieldType) {
			case BLOB :
				break;
//			case BFILE : break;
			default:
				moduleValue = null;
			}
		}
		return moduleValue;
	}

	private int getNumberType(MAP1102 field_mapping_info, int precision, int digits) {
		int type = ModuleConst.MODULE_DIGIT_0;

		if(precision > 0) {
			if(field_mapping_info.getSrc_fld_digits() == ModuleConst.MODULE_DIGIT_0) {
				if(precision < 20) type = 3; // Long
				if(precision >= 20) type = 1; // BigInteger
			} else {
				if(precision < 20) type = 4; //double
				if(precision >= 20) type = 2; // BigDecimal
			}
		}
		return type;
	}

	private String clobToString(Clob clob) throws SQLException, IOException {
		Reader reader = clob.getCharacterStream();
	    String result = IOUtils.toString(reader);
	    IOUtils.closeQuietly(reader);
	    return result;
	}

	private byte[] blobToByteArray(Blob blob) throws SQLException {
		return (blob == null ? null : blob.getBytes(0, (int) blob.length()));
	}
}
