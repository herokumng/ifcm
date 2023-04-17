package com.dkbmc.ifcm.module.data.convert;

import java.util.HashMap;
import java.util.Map;

import com.dkbmc.ifcm.module.data.convert.DataTypeDef.moduleType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.oracleType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.salesforceType;
import com.dkbmc.ifcm.module.data.convert.DataTypeDef.systemType;

import lombok.Getter;

@Getter
public class DataTypeMapper {
	private Map<salesforceType, moduleType> salesforce;
	private Map<oracleType, moduleType> oracle;

	public DataTypeMapper() {
		salesforce = null;
		oracle = null;
	}

	public void setDataTypeMapper(systemType system_type) {
		if (system_type.equals(systemType.SALESFORCE)) {
			if(salesforce == null) {
				salesforce = new HashMap<>();
				setSalseforceTypeMapping();
			}
		}
		if (system_type.equals(systemType.ORACLE)) {
			if(oracle == null) {
				oracle = new HashMap<>();
				setOracleTypeMapping();
			}
		}
	}

	private void setSalseforceTypeMapping() {

		for (salesforceType fieldType : salesforceType.values()) {
			if (fieldType.equals(salesforceType.ADDRESS)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.ANYTYPE)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.CALCULATED)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.COMBOBOX)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.CURRENCY)) salesforce.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(salesforceType.DATACATEGORYGROUPREFERENCE)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.EMAIL)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.ENCRYPTEDSTRING)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.ID)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.JUNCTIONIDLIST)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.LOCATION)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.MASTERRECORD)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.MULTIPICKLIST)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.PERCENT)) salesforce.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(salesforceType.PHONE)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.PICKLIST)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.REFERENCE)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.TEXTAREA)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.URL)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.BASE64)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.BOOLEAN)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.BYTE)) salesforce.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(salesforceType.DATE)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.DATETIME)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.DOUBLE)) salesforce.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(salesforceType.INT)) salesforce.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(salesforceType.STRING)) salesforce.put(fieldType, moduleType.STRING);
			if (fieldType.equals(salesforceType.TIME)) salesforce.put(fieldType, moduleType.STRING);
		}
	}

	private void setOracleTypeMapping() {

		for (oracleType fieldType : oracleType.values()) {
			if (fieldType.equals(oracleType.CHAR)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.VARCHAR2)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.VARCHAR)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.NCHAR)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.NVARCHAR)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.LONG)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.CLOB)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.NCLOB)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.NUMBER)) oracle.put(fieldType, moduleType.NUMBER);
			if (fieldType.equals(oracleType.FLOAT)) oracle.put(fieldType, moduleType.NUMBER);
//			if (fieldType.equals(oracleType.BINARY_FLOAT)) oracle.put(fieldType, moduleType.DOUBLE);
//			if (fieldType.equals(oracleType.BINARY_DOUBLE)) oracle.put(fieldType, moduleType.DOUBLE);
			if (fieldType.equals(oracleType.DATE)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.TIMESTAMP)) oracle.put(fieldType, moduleType.STRING);
			if (fieldType.equals(oracleType.BLOB)) oracle.put(fieldType, moduleType.STRING);
//			if (fieldType.equals(oracleType.BFILE)) oracle.put(fieldType, moduleType.BYTE_ARRAY);
		}
	}

}
