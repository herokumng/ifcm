package com.dkbmc.ifcm.module.common.log;

public class LogTypeDef {
	public enum LogLevel {
		TRACE(0), DEBUG(1), INFO(2), WARN(3), ERROR(4), FATAL(5);

		@SuppressWarnings("unused")
		private int levelCode;

		LogLevel(int level_code) {
			// TODO Auto-generated constructor stub
			this.levelCode = level_code;
		}
	}
}
