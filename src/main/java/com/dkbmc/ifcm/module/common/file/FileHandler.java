package com.dkbmc.ifcm.module.common.file;

import java.io.File;
import java.io.IOException;

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.module.common.utils.CryptoUtils;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.dkbmc.ifcm.module.data.crypto.CryptoTypeDef.CryptoProcessType;

/**
 * FileHandler 생성 - File Type에 따라 Handler 변경, Factory Method 적용. 사용 시 아래와 같이 호출
 * : FileHandler fileHandler = new HandlerCreator().create(FileType.CSV);
 *
 * 모듈 명세 : FileHandler를 통해 생성되는 각 클래스 별 타입의 파일 및 디렉토리에 대한 I/O 수행.
 * 			 open -> Read/Write/load/bulk/copy -> close
 * @category file handler factory
 * @author DY
 * @param <T>
 *
 */

public abstract class FileHandler {
	// private String directoryPath;
	// private String filePath;
	// public ArrayList<?> dataRows;
	protected File file;
	// 디렉토리 및 파일 경로

	protected String filePath;
	protected String directoryPath;
	protected CryptoUtils cryptoUtil;

	protected boolean closed;

	public FileHandler() {
		cryptoUtil = new CryptoUtils(CryptoProcessType.FILE_DATA);
	}

	// implements by extends class
	public abstract <T> T read(FileContentType file_cont_type, FileReadType file_read_type); // read only

	/**
	 *
	 * @param <T> output_line : 입력 값
	 * @param line_number : 입력 위치, 0 -> start elements / -1 -> last elements
	 * @throws IOException
	 *
	 */
	public abstract <T> boolean write(FileContentType file_cont_type, FileWriteType file_write_type, T output_content, int line_number); // single rows

	public abstract void write(String output_content);

	public abstract void close(FileUseType use_Type);

	public abstract void copy(String source_path, String target_path);

	protected File getFile() {
		return this.file;
	}

	protected String getFilePath() {
		return this.filePath;
	}

	public boolean alreadyCreated(String file_path) {
		return new File(file_path).exists();
	}

	public boolean isOpen() {
		return !closed;
	}

	public boolean open(String file_path, FileUseType use_type) {
		// TODO Auto-generated method stub
		file = new File(file_path);
		closed = false;
		if (use_type.equals(FileUseType.READ)) {
			if (alreadyCreated(file_path)) {
				return true;
			} else {
				closed = true;
				return false;
			}
		} else {
			if (alreadyCreated(file_path)) {
				return true;
			} else {
				try {
					new File(file.getParentFile().getPath()).mkdirs();
					file.createNewFile();
					return true;
				} catch (Exception e) {
					MessageHandler.getInstance().setModuleMessage("", "", "");
					closed = true;
					return false;
				}
			}
		}

	}
	/**
	 * 파일 삭제
	 * @param file_path 삭제할 파일의 Path
	 * @return 삭제 결과
	 */
	public abstract <T> boolean delete(String file_path); // single rows

	// 파일 생성용
	public String makePath(String root_name, String directory_path, String file_name) {
		directoryPath = (root_name == null ? ModuleConst.MODULE_EMPTY_REGEX : root_name + File.separator)
					  + directory_path
					  + File.separator;
		if (file_name.isEmpty())
			return directoryPath;
		else
			return (filePath = directoryPath + file_name);
	}
	public abstract void download(String filePath, String fileName);

	/*
	 * Not Used
	 * public <T> void release(T open_file) {
	 * 		try { // call reflection
	 * 			method open_file.getClass().getMethod("close").invoke(open_file);
	 * 		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { // TODO
	 * 			Auto-generated catch block
	 * 			logger.error("[FileHandler.close] : " + e.getMessage()); }
	 * }
	 *
	 * public void open(String filePath, UseType use_type) {
	 * 		try {
	 * 			if (use_type.equals(UseType.READ)) inFile = new FileInputStream(filePath);
	 * 			else outFile = new FileOutputStream(filePath);
	 * 		} catch (FileNotFoundException e) {
	 * 			// TODO Auto-generated catch block
	 * 			logger.error("[FileHandler.open] : " + e.getMessage()); }
	 * }
	 */
}
