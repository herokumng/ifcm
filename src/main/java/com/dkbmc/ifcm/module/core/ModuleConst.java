package com.dkbmc.ifcm.module.core;

/**
 * @see Module 내 공통으로 사용 값들만 정의
 * @category Const Value
 * @author DY
 *
 */
public final class ModuleConst {
	// Module Common Const
	public final static String MODULE_CHAIN_REGEX = "\\.";
	public final static String MODULE_JOIN_REGEX = "_";
	public final static String MODULE_ERASE_REGEX = "\\";
	public final static String MODULE_DIV_REGEX = ";";
	public final static String MODULE_EMPTY_REGEX = "";
	public final static String MODULE_BLANK_REGEX = " ";
	public final static String MODULE_COMMA_REGEX = ",";

	// Module Resource Get Item Key
	public final static String MODULE_KEY_ID = "id";
	public final static String MODULE_KEY_DIV = "division";
	public final static String MODULE_KEY_PATH = "path";

	public final static String MODULE_KEY_SUB = "sub_div_code";
	public final static String MODULE_KEY_DIRECTORY = "directory";
	public final static String MODULE_KEY_FILENAME = "file_name";
	public final static String MODULE_KEY_TYPE = "type";
	public final static String MODULE_KEY_CODE = "code";
	public final static String MODULE_KEY_PACK = "pack";

	// File Source Management
	public final static String FILE_DIV_DOWNLOAD = "download";
	public final static String FILE_DIV_UPLOAD = "upload";
	public final static String FILE_TYPE_JSON = "json";
	public final static String FILE_TYPE_CSV = "csv";

	// Module Common Digit Const
	public final static int MODULE_DIGIT_0 = 0;
	public final static int MODULE_DIGIT_1 = 1;
	public final static int MODULE_DIGIT_2 = 2;
	public final static int MODULE_DIGIT_3 = 3;
	public final static int MODULE_DIGIT_4 = 4;
	public final static int MODULE_DIGIT_5 = 5;
	public final static int MODULE_DIGIT_6 = 6;
	public final static int MODULE_DIGIT_7 = 7;
	public final static int MODULE_DIGIT_8 = 8;
	public final static int MODULE_DIGIT_9 = 9;

	public final static String MODULE_CHOICE_YES = "Y";
	public final static String MODULE_CHOICE_NO = "N";

	// Using For Reflection Method
	public final static String REFLECTION_METHOD_SET = "set";
	public final static String REFLECTION_METHOD_GET = "get";
}
