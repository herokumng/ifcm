package com.dkbmc.ifcm.module.common.log;

import com.dkbmc.ifcm.dto.log.LOG1001;
import com.dkbmc.ifcm.module.common.log.LogTypeDef.LogLevel;
import com.dkbmc.ifcm.module.common.utils.SystemChecker;

/**
 *
 * @author DY
 *
 */
public class Logger {
	private LogSaver logSaver;
	private LogLevel logLevel;
	
	public Logger(LOG1001 log1001_saveForm, String root_log_path, String log_level) {
		logSaver = new LogSaver(log1001_saveForm, root_log_path);
		for(LogLevel level : LogLevel.values()) {
			if(level.ordinal() == Integer.valueOf(log_level)) {
				logLevel = level;
			}
		}
	}

	// out console log
	public void printLog(String log_level, String message) {
		LogLevel messageLoglevel = LogLevel.valueOf(log_level);
		if(logLevel.ordinal() <= messageLoglevel.ordinal()) {
			System.out.println(message);
			System.out.println(SystemChecker.getInstance().currentMemoryUsage());
		}
	}

	// write log
	public void writeLog(String log_level, String message) {
		LogLevel messageLoglevel = LogLevel.valueOf(log_level);
		if(logLevel.ordinal() <= messageLoglevel.ordinal()) {
			logSaver.writeLogMessage(message);
		}
		
	}

	// close log File
	public void saveLog() {
		logSaver.saveLogMessage();
	}
}
