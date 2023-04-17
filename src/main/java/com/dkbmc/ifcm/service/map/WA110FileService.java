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
public class WA110FileService implements IWAFileService {
    private MappingProcess mappingProcess;

    public WA110FileService(ObjectMapper objectMapper) {
    	mappingProcess = MappingProcess.getInstance();
        mappingProcess.setObjectMapper(objectMapper);
    }

    @Override
    public ResponseDTO SVC001() {
        return null;
    }

    @Override
    public ResponseDTO SVC101(Map<String, Object> request) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0006", null);
        return mappingProcess.wa110SVC101((String) request.get("id"));
    }

    @Override
    public ResponseDTO SVC201(Map<String, Object> request) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0007", null);
        return mappingProcess.wa110SVC201(request);
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
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0008", null);
        return mappingProcess.wa110SVC601((String) request.get("srcSysCode"), (String) request.get("tgtSysCode"), (String) request.get("tgtObjName"));
    }

	@Override
	public ResponseDTO SVC000(Map<String, Object> request) {
		// TODO Auto-generated method stub
		return null;
	}


}
