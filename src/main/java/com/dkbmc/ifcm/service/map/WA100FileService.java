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
public class WA100FileService implements IWAFileService {
    private MappingProcess mappingProcess;

    public WA100FileService(ObjectMapper objectMapper) {
    	mappingProcess = MappingProcess.getInstance();
        mappingProcess.setObjectMapper(objectMapper);
    }

    @Override
    public ResponseDTO SVC001() {
        return null;
    }

    @Override
    public ResponseDTO SVC101(Map<String, Object> request) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0002", null);
        return mappingProcess.wa100SVC101(request);
    }

    @Override
    public ResponseDTO SVC201(Map<String, Object> request) {
        return null;
    }

    @Override
    public ResponseDTO svc401(String mapId) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0003", null);
        return mappingProcess.wa100SVC401(mapId);
    }

    @Override
    public void SVC501(String mapId) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0004", null);
        mappingProcess.wa100SVC501(mapId);
    }

    @Override
    public ResponseDTO SVC601(Map<String, Object> request, MultipartFile file) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0005", null);
        return mappingProcess.wa100SVC601(file, (String) request.get("id"));
    }

	@Override
	public ResponseDTO SVC000(Map<String, Object> request) {
		// TODO Auto-generated method stub
		return null;
	}
}