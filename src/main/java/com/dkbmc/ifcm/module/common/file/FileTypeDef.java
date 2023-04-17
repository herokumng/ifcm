package com.dkbmc.ifcm.module.common.file;

public class FileTypeDef {
	public enum FileType {
		META, CSV, EXCEL, LOG
	}

	public enum  FileUseType {
		READ, WRITE, COPY, DOWNLOAD
	}

	public enum FileContentType {
		HEADER, DATA
	}

	public enum FileReadType {
		LOAD, ROW, BUFFER
	}

	public enum FileWriteType {
		BULK, ROW, DECRYPTION, APPEND
	}
}