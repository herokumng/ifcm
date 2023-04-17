package com.dkbmc.ifcm.module.common.file;

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;

/**
 * @category file handler factory
 * @implNote Excel Type File Handler for upload data
 * @author DY
 *
 */
public class ExcelFile extends FileHandler {

	public ExcelFile() {
		super();
	}

	@Override
	public <T> T read(FileContentType file_cont_type, FileReadType file_read_type) {
		return null;
		// TODO Auto-generated method stub

	}

	@Override
	public <T> boolean write(FileContentType file_cont_type, FileWriteType file_write_type, T output_content, int line_number) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void close(FileUseType use_Type) {
		// TODO Auto-generated method stub

	}

	@Override
	public void copy(String source_path, String target_path) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> boolean delete(String file_path) {
		return false;
	}

	@Override
	public void download(String filePath, String fileName) {}

	@Override
	public void write(String output_content) {
		// TODO Auto-generated method stub

	}

}
