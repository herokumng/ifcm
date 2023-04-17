package com.dkbmc.ifcm.module.core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleProperties {
	private String ROOT_META_PATH;	// meta file root path
	private String ROOT_CLASS_PATH;	// class file root package path
	private String ROOT_LOG_PATH;	// log directory root path

	private String INITIAL_LOAD_DIRECTORY;
	private String INITIAL_LOAD_PACKAGE;
	private String INITIAL_LOAD_FILE;
	private String INITIAL_FILE_IDS;
	private String CRUD_FILE_IDS;

	private String MAPPING_FILE_IDS;

	private String MAPPING_FIELD_IDS;

	private String SALESFORCE_SYSTEM_ID;

	private String MODULE_LOG_LEVEL;

	private String CONVERT_DATETIME_PATTERN;
}
