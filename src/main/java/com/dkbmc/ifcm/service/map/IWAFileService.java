package com.dkbmc.ifcm.service.map;

import com.dkbmc.ifcm.dto.common.ResponseDTO;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IWAFileService {
	ResponseDTO SVC000(Map<String, Object> request);
	
    ResponseDTO SVC001();

    ResponseDTO SVC101(Map<String, Object> request);

    ResponseDTO SVC201(Map<String, Object> request);

    ResponseDTO svc401(String mapId);

    void SVC501(String mapId);

    ResponseDTO SVC601(Map<String, Object> request, MultipartFile file);


}
