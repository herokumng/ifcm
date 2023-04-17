package com.dkbmc.ifcm.module.data.convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import com.dkbmc.ifcm.dto.mapping.MAP1102;
import com.dkbmc.ifcm.module.core.ModuleConst;

public abstract class CommonConverter {
	protected DataTypeMapper dataTypeMapper;
	protected String[] datePatterns;
	private List<String> patterns;
	// private final static String source = "src", target = "tgt", field = "fld",
	// precision = "precision", digits = "digits";

	public CommonConverter(DataTypeMapper data_type_mapper, String date_patterns) {
		dataTypeMapper = data_type_mapper;
		datePatterns = date_patterns.split(ModuleConst.MODULE_DIV_REGEX);
	}

	public abstract Object conversionSystemToModule(MAP1102 field_mapping_info, Object field_data)
			throws IllegalArgumentException, SQLException, IOException;

	public abstract Object conversionModuleToSystem(MAP1102 field_mapping_info, Object field_data)
			throws SQLException, IOException;

	protected long conversionToLong(String number) {
		return Long.valueOf(number);
	}

	protected Double conversionToDouble(String number) {
		return Double.valueOf(number);
	}

	protected BigInteger conversionToBigInteger(String number) {
		return new BigInteger(number);
	}

	protected BigDecimal conversionToBigDecimal(String number) {
		return new BigDecimal(number);
	}

	protected Boolean conversionIntStringToBoolean(String int_value) {
		if (int_value == null || int_value == ModuleConst.MODULE_EMPTY_REGEX)
			return null;

		if (int_value.equals(String.valueOf(ModuleConst.MODULE_DIGIT_1)))
			return true;
		else if (int_value.equals(String.valueOf(ModuleConst.MODULE_DIGIT_0)))
			return false;
		else if (int_value.equals(ModuleConst.MODULE_CHOICE_YES))
			return true;
		else if (int_value.equals(ModuleConst.MODULE_CHOICE_NO))
			return false;
		else
			return Boolean.valueOf(int_value);
	}

	private boolean isCorrectDateTimePattern(String date_value, String date_pattern) {
		try {
			//LocalDateTime.parse(date_string, DateTimeFormatter.ofPattern(date_pattern));
			SimpleDateFormat format = new SimpleDateFormat(date_pattern);
			format.parse(date_value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void setSystemDateTimePatterns() {
		if(patterns == null) patterns = Arrays.asList(datePatterns);
	}

	protected String conversionToDate(String date_value) {
		setSystemDateTimePatterns();
		String conversionDate = null;

		for (String pattern : patterns) {
			if (isCorrectDateTimePattern(date_value, pattern)) {
				//__Debug__ System.out.println(String.format("pattern : %s, date_value : %s", pattern, date_value));
				LocalDate date = LocalDate.parse(date_value, DateTimeFormatter.ofPattern(pattern));
				conversionDate = date.format(DateTimeFormatter.ISO_DATE);
				break;
			}
		}
		return conversionDate;
	}

	protected String conversionToTime(String date_value) {
		setSystemDateTimePatterns();
		String conversionDate = null;

		for(String pattern : patterns) {
			if(isCorrectDateTimePattern(date_value, pattern)) {
				LocalTime datetime = LocalTime.parse(date_value, DateTimeFormatter.ofPattern(pattern));
				conversionDate = datetime.format(DateTimeFormatter.ISO_TIME);
				break;
			}
		}
		return conversionDate;
	}

	protected String conversionToDatetime(String date_value) {
		setSystemDateTimePatterns();
		String conversionDate = null;

		for(String pattern : patterns) {
			if(isCorrectDateTimePattern(date_value, pattern)) {
				LocalDateTime datetime = LocalDateTime.parse(date_value, DateTimeFormatter.ofPattern(pattern));
				conversionDate = datetime.format(DateTimeFormatter.ISO_DATE_TIME);
				break;
			}
		}
		return conversionDate;
	}

	protected boolean isExistDatePattern(String date_string) {
		return Arrays.asList(datePatterns).stream().anyMatch(pattern -> {
			try {
				LocalDateTime.parse(date_string, DateTimeFormatter.ofPattern(pattern));
				return true;
			} catch (Exception e) {
				return false;
			}
		});
	}
}
