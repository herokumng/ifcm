package com.dkbmc.ifcm.service.map;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.module.mapping.MappingProcess;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.logging.LogLevel;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class WA000FileService implements IWAFileService {
    private MappingProcess mappingProcess;

    public WA000FileService(ObjectMapper objectMapper) {
        mappingProcess = MappingProcess.getInstance();
        mappingProcess.setObjectMapper(objectMapper);
    }
    
    @Override
	public ResponseDTO SVC000(Map<String, Object> request) {
		// TODO Auto-generated method stub
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0000", null);
    	return mappingProcess.wa000SVC000(request);
	}
    
    @Override
    public ResponseDTO SVC001() {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0001", null);
    	return mappingProcess.wa000SVC001();
    }

    @Override
    public ResponseDTO SVC101(Map<String, Object> request) {
        return null;
    }

    @Override
    public ResponseDTO SVC201(Map<String, Object> request) {
        return null;
    }

    @Override
    public ResponseDTO svc401(String mapId) {
        return null;
    }

    @Override
    public void SVC501(String mapId) {

    }

    @Override
    public ResponseDTO SVC601(Map<String, Object> request, MultipartFile file) {
        return null;
    }

	
}
