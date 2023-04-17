package com.dkbmc.ifcm.module.common.file;

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileType;

/**
 * @category file handler factory
 * @author DY
 *
 */
public class HandlerCreator {
	public FileHandler create(FileType fileType) {
		switch(fileType) {
		case META :
			return new MetaFile();
		case EXCEL :
			return new ExcelFile();
		case CSV :
			return new CSVFile();
		case LOG :
			return new LogFile();
		default :
			throw new IllegalArgumentException("Unexpected value: " + fileType.toString());
		}
	}
}
