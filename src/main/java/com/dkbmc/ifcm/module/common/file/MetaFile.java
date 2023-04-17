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

import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.common.message.MessageHandler;

/**
 * @author DY
 * @category file handler factory
 * @implNote Json Type File Handler for System meta
 */

public class MetaFile extends FileHandler {
    private BufferedReader reader;
    private BufferedWriter writer;

    public MetaFile() {
        super();
    }

    private void createReader() throws FileNotFoundException {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(getFile()), StandardCharsets.UTF_8));
    }

    private void createWriter() throws FileNotFoundException {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(getFile()), StandardCharsets.UTF_8));
    }

    /**
     * Meta File(Json)의 경우 전체 로드 : read(FileContentType.DATA, FileReadType.LOAD);
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(FileContentType file_cont_type, FileReadType file_read_type) {
        // TODO Auto-generated method stub
        try {
            if (reader == null) createReader();
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                //line = cryptoUtil.dataDecryption(line);
                builder.append(line);
            }
            return (T) builder;
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
            return null;
        }
    }

    /**
     * Meta File(Json)의 경우 전체 변경 : read(FileContentType.DATA, FileWriteType.LOAD);
     */
    @Override
    public <T> boolean write(FileContentType file_cont_type, FileWriteType file_write_type, T output_content, int line_number) {
        // TODO Auto-generated method stub
        try {
            if (writer == null) createWriter();
            //writer.write(cryptoUtil.dataEncryption(output_content.toString()));
            writer.write(output_content.toString());
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
            return false;
        }
    }

	@Override
	public void close(FileUseType use_Type) {
		// TODO Auto-generated method stub
		try {

			if(use_Type.equals(FileUseType.READ) && reader != null) {
				reader.close();
			}

			if(use_Type.equals(FileUseType.WRITE) && writer != null) {
				writer.flush();
				writer.close();
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

        try {
            file.delete();
            return true;
        } catch (Exception e) {
            MessageHandler.getInstance().setModuleMessage("", "", e.getStackTrace());
            return false;
        }
    }

    @Override
    public void download(String filePath, String fileName) {

    }

    @Override
	public void write(String output_content) {
		// TODO Auto-generated method stub

	}
}