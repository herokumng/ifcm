package com.dkbmc.ifcm.module.common.log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.dkbmc.ifcm.dto.log.LOG1001;
import com.dkbmc.ifcm.module.common.file.FileHandler;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.common.file.HandlerCreator;
import com.dkbmc.ifcm.module.common.utils.IdGenerator;
import com.dkbmc.ifcm.module.core.ModuleConst;

public class LogSaver {
	private FileHandler fileHandler;
	private String rootDirectory, fileName, filePath;
	private LOG1001 log1001;

	public LogSaver(LOG1001 form_log1001, String log_directory_path) {
		rootDirectory = log_directory_path;
		log1001 = form_log1001;
		fileHandler = new HandlerCreator().create(FileType.LOG);
		initializeLogFile();
		createLogFile();
	}

	private void initializeLogFile() {
		// 20230102.log
		fileName = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
				+ ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX)
				+ LogConst.LOG_EXTENSION_REGEX;

		// default Module log file path : ./log/yyyyMMdd
		filePath = fileHandler.makePath(getDefaultPath(), rootDirectory, fileName);
		// default Module log manage file path : ./log/log1001
	}
	
	private String getDefaultPath() {
		return (rootDirectory.equals(LogConst.LOG_EXTENSION_REGEX) ? ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX) : null);
	}
	
	private void createLogFile() {
		if (!fileHandler.alreadyCreated(filePath)) {
			createLogManageFile();
			fileHandler.open(filePath, FileUseType.WRITE);
		}

		if(!fileHandler.isOpen()) {
			fileHandler.open(filePath, FileUseType.WRITE);
		}
	}

	private void createLogManageFile() {
		FileHandler manageFileHandler = new HandlerCreator().create(FileType.CSV);
		String logManageFilePath = manageFileHandler.makePath(getDefaultPath(), rootDirectory, log1001.getClass().getSimpleName().toLowerCase());
		
		if (manageFileHandler.open(logManageFilePath, FileUseType.WRITE)) {

			log1001.setId(IdGenerator.getInstance().getId());
			log1001.setName(fileName);
			log1001.setPath(filePath);
			manageFileHandler.write(FileContentType.DATA, FileWriteType.APPEND, log1001.toString() + System.lineSeparator(), -1);
			manageFileHandler.close(FileUseType.WRITE);
		}
	}
	
	protected void writeLogMessage(String log_message) {
		fileHandler.write(log_message);
	}

	protected void saveLogMessage() {
		fileHandler.close(FileUseType.WRITE);
	}
}
