package com.dkbmc.ifcm.controller.map;

import com.dkbmc.ifcm.service.map.WA000FileService;
import io.netty.handler.logging.LogLevel;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.service.map.IWAFileService;

@Controller
@RequestMapping("/map/wa000")
public class WA000 {
    private final IWAFileService iWAFileService;

    public WA000(WA000FileService wa000FileService) {
        iWAFileService = wa000FileService;
    }

	@PostMapping ("/svc000")
    public ResponseEntity <ResponseDTO> SVC000(@RequestBody  HashMap<String, Object> request){
		MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0000", null);
		return ResponseEntity.ok().body(iWAFileService.SVC000(request));
    }
    
    @GetMapping ("/svc001")
    public ResponseEntity <ResponseDTO> SVC001(){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0001", null);
    	return ResponseEntity.ok().body(iWAFileService.SVC001());
    }
}
