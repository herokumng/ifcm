package com.dkbmc.ifcm.module.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.dkbmc.ifcm.module.common.log.LogConst;
import com.dkbmc.ifcm.module.common.log.Logger;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.module.common.parse.MetaParser;
import com.dkbmc.ifcm.module.common.utils.MetaUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class ModuleOperation {
	private final String MODULE_PROPERTY_PATH = "config/module.properties";

	private ModuleProperties properties;
	private MetaUtils metaUtil;
	private ModuleResource moduleResource;
	private String fileRoot, classRoot, classPath, division, fileName, filePath, type, fileDirPath, path;
//	public static void main(String[] args) {
//		ModuleOperation operation = new ModuleOperation();
//		operation.startup();
//	}
	
	public ModuleOperation() {
		properties = new ModuleProperties();
		metaUtil = new MetaUtils();
		moduleResource = new ModuleResource();
	}

	public ModuleProperties getProperties() {
		return this.properties;
	}

	public ModuleResource getModuleResource() {
		return this.moduleResource;
	}

	public String getClassPath() {
		return this.classPath;
	}

	public String getFileDirPath() {
		return this.fileDirPath;
	}

	public String getFilePath() {
		return this.filePath;
	}
	
	public void setUserInfo(Map<String, Object> user_info) {
		MessageHandler.getInstance().setUserInfo(user_info);
	}
	
	/**
	 * # satrtup Order 1. 프로퍼티 로드 2. 시스템 파일 정보 로드 3. 스스템 파일 정보에서 시스템 구동 필수 파일 정보 검색
	 * 4. 해당 필수 파일 정보 로드
	 *
	 */
	public void startup() {
		try {
			loadProperties();
			initialize();
			loadResource();

			String logPath = properties.getROOT_LOG_PATH();
//			__Debug__ System.out.println("logPath : " + logPath);
			
			if(logPath == null || logPath.isBlank() || logPath.isEmpty()) {
				logPath = (String) metaUtil.getMetaEntityItemValue(moduleResource.getSys1001(),
						ModuleConst.MODULE_KEY_DIV, LogConst.LOG_EXTENSION_REGEX, ModuleConst.MODULE_KEY_DIV);
			}
			Logger logger = new Logger(moduleResource.getLog1001(), logPath, properties.getMODULE_LOG_LEVEL());
			MessageHandler.getInstance().setLogResource(moduleResource.getSys3001(), moduleResource.getSys3002(), logger); // injection resource to Message Handler
//			 __Debug__ System.out.println(modProp.toString());
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException | IOException | ClassNotFoundException | InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initialize() {
		fileRoot = ModuleConst.MODULE_CHAIN_REGEX.replace(
				ModuleConst.MODULE_ERASE_REGEX,ModuleConst.MODULE_EMPTY_REGEX)
				+ File.separator
				+ properties.getROOT_META_PATH()
				+ File.separator;

		classRoot = properties.getROOT_CLASS_PATH()
				+ ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX);

		filePath = fileRoot + properties.getINITIAL_LOAD_DIRECTORY()
				+ File.separator
				+ properties.getINITIAL_LOAD_FILE();

		classPath = classRoot + properties.getINITIAL_LOAD_PACKAGE()
				+ ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX)
				+ (properties.getINITIAL_LOAD_FILE().toUpperCase());
	}

	// Module Terminate
	public void terminate() {
		// resource release
		moduleResource.clearResource();
	}

	// SYS1001 File-시스템파일경로 정보 로드
	private void loadResource()
			throws IOException, ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
		// eClipse 상대 경로 셋팅
		moduleResource.setSys1001(getResourceList(metaUtil.getMetaEntityClass(classPath)));
		// __DEBUG__System.out.println(resource.getSys1001().get(0).getCreated_datetime());
		loadModuleResourceInfo(moduleResource.getSys1001());
	}

	// LOG1001-로그 파일 양식, SYS2001-로그 파일 관리, SYS3001-타입별 코드/출력 매시지 정보, SYS4001-레가시 시스템
	// 정보, CAT1001-카테고리 정보 로드
	private <T> void loadModuleResourceInfo(List<T> sys1001)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException, ClassNotFoundException, IOException {

		Object[] recordId = properties.getINITIAL_FILE_IDS().split(ModuleConst.MODULE_DIV_REGEX);

		for (int index = ModuleConst.MODULE_DIGIT_0; index < recordId.length; index++) {
			setResourcePathForFile(sys1001, recordId[index]);

			if (index == ModuleConst.MODULE_DIGIT_0) { // LOG1001
				moduleResource.setLog1001(metaUtil.getMetaEntity(classPath));
				// __DEBUG__System.out.println(moduleResource.getLog1001());
			}

			if (index == ModuleConst.MODULE_DIGIT_1) { // SYS2001 - login form
				moduleResource.setSys2001(metaUtil.getMetaEntity(classPath));
				// __DEBUG__System.out.println(moduleResource.getSys2001());
			}

			if (index == ModuleConst.MODULE_DIGIT_2) { // SYS3001
				moduleResource.setSys3001(getResourceList(metaUtil.getMetaEntityClass(classPath)));
				// __DEBUG__System.out.println(moduleResource.getSys3001());
			}

			if (index == ModuleConst.MODULE_DIGIT_3) { // SYS4001
				moduleResource.setSys4001(getResourceList(metaUtil.getMetaEntityClass(classPath)));
				// __DEBUG__System.out.println(moduleResource.getSys4001());
			}

			if (index == ModuleConst.MODULE_DIGIT_4) { // CAT1001
				moduleResource.setCat1001(getResourceList(metaUtil.getMetaEntityClass(classPath)));
				// __DEBUG__System.out.println(moduleResource.getCat1001());
			}

			if (index == ModuleConst.MODULE_DIGIT_5) { // SYS3002
				moduleResource.setSys3002(getResourceList(metaUtil.getMetaEntityClass(classPath)));
				// __DEBUG__System.out.println(moduleResource.getSys3001());
			}
		}
		// System.getProperty("os.name").toLowerCase().contains("win");
	}

	public <T> List<T> getResourceList(Class<?> meta_class)
			throws JsonMappingException, JsonProcessingException, IOException {
		List<T> resourcelist = new MetaParser<T>().getMetaEntityEntries(metaUtil.getMetaDataFromFile(filePath),
				meta_class.getCanonicalName());
		return (resourcelist == null ? null : resourcelist);
	}

	@SuppressWarnings("unchecked")
	public <T> void setResourcePathForFile(List<T> sys1001, Object record_id)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException {

		T dataRow = (T) metaUtil.getMetaEntityData(sys1001, ModuleConst.MODULE_KEY_ID, record_id);
		// __DEBUG__ System.out.println(essentialDataRow);

		division = (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_DIV);
		fileName = (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_FILENAME);

		type = (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_TYPE);

		path = (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_PATH);

		fileDirPath = fileRoot + path;

		filePath = fileRoot + division + File.separator + fileName;
		classPath = classRoot + (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_PACK)
				+ ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX)
				+ fileName.toUpperCase();
	}

	@SuppressWarnings("unchecked")
	public <T> void setResourcePathForDirectory(List<T> sys1001, String directory_name, String file_name)
			throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException,
			SecurityException, InstantiationException {

		T dataRow = (T) metaUtil.getMetaEntityData(sys1001, ModuleConst.MODULE_KEY_DIRECTORY, directory_name);

		filePath = fileRoot + (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_PATH)
				+ file_name;

//		classPath = classRoot + classRoot
		classPath = classRoot
				+ (String) metaUtil.getMetaEntityItemValue(dataRow, ModuleConst.MODULE_KEY_PACK)
				+ ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX)
				+ directory_name.toUpperCase();
	}

	// Load Properties and Set all fields of ModuleProperties
	private void loadProperties() throws FileNotFoundException, IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		Properties prop = new Properties();

		InputStream input = new FileInputStream(MODULE_PROPERTY_PATH);

		prop.load(input);

		for (Object key : prop.keySet()) {
			List<String> listKey = Arrays.asList(key.toString().toUpperCase().split(ModuleConst.MODULE_CHAIN_REGEX));

			Collections.reverse(listKey);

			String propField = String.join(ModuleConst.MODULE_JOIN_REGEX, listKey);

			properties.getClass()
					.getMethod(ModuleConst.REFLECTION_METHOD_SET + propField, String.class)
					.invoke(properties, prop.getProperty((String) key));
		}
		// release resource
		input.close();
	}
}
