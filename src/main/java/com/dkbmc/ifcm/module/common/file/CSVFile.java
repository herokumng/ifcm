package com.dkbmc.ifcm.module.common.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.common.message.MessageHandler;

import com.dkbmc.ifcm.module.core.ModuleConst;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @author DY
 * @category file handler factory
 * @implNote CSV Type File Handler for upload data
 */
public class CSVFile extends FileHandler {
    private BufferedReader reader;
    private BufferedWriter writer;

    private BufferedInputStream input;
    private BufferedOutputStream output;
    private int index;
	public CSVFile() {
		super();
	}

	private void createReader() throws FileNotFoundException {
		reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8));
		index = 0;
	}

	private void createWriter(FileWriteType file_write_type) throws FileNotFoundException {
		switch(file_write_type) {
		case APPEND :
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile(), true), StandardCharsets.UTF_8));
			break;
		default :
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), StandardCharsets.UTF_8));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T read(FileContentType file_cont_type, FileReadType file_read_type) {
		// TODO Auto-generated method stub
		String line = null;
		List<String> builder = null;
		try {
			if(reader == null) createReader();

			if(file_read_type.equals(FileReadType.LOAD)) builder = new ArrayList<>();
			
			while ((line = reader.readLine()) != null) {
				// line = cryptoUtil.dataDecryption(line);
				if(file_read_type.equals(FileReadType.ROW) && file_cont_type.equals(FileContentType.HEADER) && index == 0) {
					index++;
					break;
				}

				if(file_read_type.equals(FileReadType.LOAD) && file_cont_type.equals(FileContentType.DATA) && index > 0) {
					builder.add(line);
				}
				index++;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MessageHandler.getInstance().setModuleMessage("", "",  e.getStackTrace());
		}

		return file_read_type.equals(FileReadType.LOAD) ? (T) builder : (T) line;
	}

	@Override
	public <T> boolean write(FileContentType file_cont_type, FileWriteType file_write_type, T output_content, int line_number) {
		// TODO Auto-generated method stub
		try {
			if(writer == null) createWriter(file_write_type);

			switch(file_write_type) {
			case DECRYPTION :
				writer.write(String.valueOf(output_content));
				break;
			default :
				// writer.write(cryptoUtil.dataEncryption(output_content.toString())); //암호화 적용 후 변경.
				writer.write(String.valueOf(output_content));
			}

			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
			return false;
		}
	}

	@Override
	public void close(FileUseType use_Type) {
		// TODO Auto-generated catch block
		try {

			if(use_Type.equals(FileUseType.READ) && reader != null) {
				reader.close();
			}

			if(use_Type.equals(FileUseType.WRITE) && writer != null) {
				writer.flush();
				writer.close();
			}

            if (use_Type.equals(FileUseType.DOWNLOAD) && output != null && input != null) {
                input.close();
                output.close();
            }

			closed = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
		}
    }

    @Override
    public void copy(String source_path, String target_path) {
        // TODO Auto-generated method stub

	}

	@Override
	public <T> boolean delete(String file_path) {
		File csvFile = new File(file_path);
		File[] folder_list;
		try {
			if (csvFile.isFile()) {
				folder_list = new File[1];
				folder_list[0] = csvFile;
			} else {
				folder_list = csvFile.listFiles();
			}
			for (int i = ModuleConst.MODULE_DIGIT_0; i < folder_list.length; i++) {
				if (folder_list[i].isFile()) {
					folder_list[i].delete();
				} else {
					delete(folder_list[i].getPath());
				}
				folder_list[i].delete();
			}
			if (csvFile.delete()) {
                return true;
			} else {
				return false;
			}

		} catch (Exception e) {
            MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
			return false;
		}
	}

    @Override
    public void download(String filePath, String fileName) {
        try {
            ServletRequestAttributes servletContainer = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpServletResponse servletResponse = servletContainer.getResponse();
            int fileSize = (int) file.length();
            if (fileSize > 0) {
                String encodedFileName = "attachment; filename*=" + "UTF-8" + "''" + URLEncoder.encode(fileName
                        + ModuleConst.MODULE_CHAIN_REGEX.replace(ModuleConst.MODULE_ERASE_REGEX, ModuleConst.MODULE_EMPTY_REGEX)
                        + ModuleConst.FILE_TYPE_CSV, "UTF-8");
                servletResponse.setContentType("application/octet-stream; charset=utf-8");
                servletResponse.setHeader("Content-Disposition", encodedFileName);
                servletResponse.setContentLengthLong(fileSize);

                input = new BufferedInputStream(new FileInputStream(file));

                output = new BufferedOutputStream(servletResponse.getOutputStream());

                try {
                    byte[] buffer = new byte[4096];
                    int bytesRead = 0;
                    while ((bytesRead = input.read(buffer)) != -ModuleConst.MODULE_DIGIT_1) {
                        output.write(buffer, 0, bytesRead);
                    }
                    output.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                MessageHandler.getInstance().setModuleMessage("", "", null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void write(String output_content) {
        // TODO Auto-generated method stub

    }
}
