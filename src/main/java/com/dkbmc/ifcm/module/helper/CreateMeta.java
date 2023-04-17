package com.dkbmc.ifcm.module.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.dkbmc.ifcm.dto.system.SYS3001;
import com.dkbmc.ifcm.dto.system.SYS3002;
import com.dkbmc.ifcm.module.common.file.FileHandler;
import com.dkbmc.ifcm.module.common.file.HandlerCreator;
import com.dkbmc.ifcm.module.common.parse.MetaParser;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileContentType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileReadType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileUseType;
import com.dkbmc.ifcm.module.common.file.FileTypeDef.FileWriteType;
import com.dkbmc.ifcm.module.core.ModuleConst;
import com.fasterxml.jackson.core.JsonProcessingException;

public class CreateMeta {
	private FileHandler fileHandler;
	private String header;
	
	public static void main(String[] args) throws JsonProcessingException {
		String filePath = "./meta/sys/sys3001";
		CreateMeta creator = new CreateMeta();
		//creator.writeMeta(creator.read3002(), filePath);
		creator.writeMeta(creator.read3001(), filePath);
	}
	
	private <T> void writeMeta(List<SYS3001> meta, String filePath) throws JsonProcessingException {
		FileHandler fileHandler = new HandlerCreator().create(FileType.META);
		fileHandler.open(filePath, FileUseType.WRITE);
		fileHandler.write(FileContentType.DATA, FileWriteType.BULK, new MetaParser<SYS3001>().setMetaEntityEntries(meta), 0);
		fileHandler.close(FileUseType.WRITE);
	}
	
	private List<SYS3002> read3002(){
		String filePath = "./rawdata/sys3002_data.csv";
		List<SYS3002> entries = new ArrayList<>();
		
		ArrayList<String> dataEntries = readFile(filePath);
		fileHandler.close(FileUseType.READ);
		
		String[] headerItems = header.split(ModuleConst.MODULE_COMMA_REGEX);
		
		for (String dataLine : dataEntries) {
			String[] lineItems = parseData(dataLine);
			if(headerItems.length == lineItems.length) {
				SYS3002 sys3002 = new SYS3002();
				for(int index=ModuleConst.MODULE_DIGIT_0; index < lineItems.length; index++) {
					if(index==0) sys3002.setId(lineItems[index].toString());
					if(index==1) sys3002.setClassname(lineItems[index].toString());
					if(index==2) sys3002.setMethod(lineItems[index].toString());
					if(index==3) sys3002.setType(lineItems[index].toString());
					if(index==4) sys3002.setAction(lineItems[index].toString());
					if(index==5) sys3002.setCreated_datetime(LocalDateTime.parse((String) lineItems[index].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
					if(index==6) sys3002.setLast_modified_datetime(LocalDateTime.parse((String) lineItems[index].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
				}
				entries.add(sys3002);
			}
		}
		return entries;
	}
	
	private List<SYS3001> read3001(){
		String filePath = "./rawdata/sys3001_data.csv";
		List<SYS3001> entries = new ArrayList<>();
		
		ArrayList<String> dataEntries = readFile(filePath);
		fileHandler.close(FileUseType.READ);
		
		String[] headerItems = header.split(ModuleConst.MODULE_COMMA_REGEX);
		
		for (String dataLine : dataEntries) {
			String[] lineItems = parseData(dataLine);
			if(headerItems.length == lineItems.length) {
				SYS3001 sys3001 = new SYS3001();
				for(int index=ModuleConst.MODULE_DIGIT_0; index < lineItems.length; index++) {
					if(index==0) sys3001.setId(lineItems[index].toString());
					if(index==1) sys3001.setType(lineItems[index].toString());
					if(index==2) sys3001.setCode(lineItems[index].toString());
					if(index==3) sys3001.setMessage(lineItems[index].toString());
					if(index==4) sys3001.setCreated_datetime(LocalDateTime.parse((String) lineItems[index].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
					if(index==5) sys3001.setLast_modified_datetime(LocalDateTime.parse((String) lineItems[index].toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")));
				}
				entries.add(sys3001);
			}
		}
		return entries;
	}
	
	private ArrayList<String> readFile(String filePath){
		fileHandler = new HandlerCreator().create(FileType.CSV);
		fileHandler.open(filePath, FileUseType.READ);
		header = fileHandler.read(FileContentType.HEADER, FileReadType.ROW);
		return fileHandler.read(FileContentType.DATA, FileReadType.LOAD);
	}
	
	private String[] parseData(String data_line) {
		String[] lineItmes = data_line.toString().split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
		return lineItmes;
	}
}
