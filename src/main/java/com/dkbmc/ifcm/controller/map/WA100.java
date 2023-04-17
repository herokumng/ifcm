package com.dkbmc.ifcm.controller.map;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.service.map.IWAFileService;
import com.dkbmc.ifcm.service.map.WA100FileService;

import io.netty.handler.logging.LogLevel;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Api(tags = {"Mapping List"})
@Controller
@RequestMapping("/map/wa100")
public class WA100 {
    private final IWAFileService iWAFileService;

    public WA100(WA100FileService wa100FileService) {
        iWAFileService = wa100FileService;
    }

    @GetMapping({"/", ""})
    public String main() {
        return "map/WA100";
    }

    /**
     * 매핑 리스트 호출 기능
     *
     * @param request cat_code 카테고리 코드, src_sys_code 소스시스템코드, tgt_sys_code 타켓시스템코드
     *                ,search_category 검색어 카테고리 , search_txt 검색내용,start_dt 시작일, end_dt 종료일
     * @return 검색된 매핑 리스트
     */
    @GetMapping("/svc101")
    public ResponseEntity<ResponseDTO> SVC101(@RequestParam HashMap<String, Object> request) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0002", null);
    	return ResponseEntity.ok().body(iWAFileService.SVC101(request));
    }

    /**
     * 매핑삭제 프로그램
     *
     * @param mapId (mapping_id)
     * @return 삭제 결과
     */
    @DeleteMapping("/svc401")
    public ResponseEntity<ResponseDTO> SVC401(@RequestParam("id") String mapId){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0003", null);
    	return ResponseEntity.ok().body(iWAFileService.svc401(mapId));
    }

    /**
     * 맵핑파일 다운로드
     * @param mapId 맵핑 아이디
     */
    @GetMapping("/svc501")
    public void SVC501(@RequestParam("id") String mapId) {
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0004", null);
    	iWAFileService.SVC501(mapId);
    }

    /**
     * 맵핑파일 업로드, 타겟오브젝트 데이터 업로드
     * @param request file 업로드 맵핑파일, id 맵핑아이디
     * @return 업로드 결과
     */
    @PatchMapping("/svc601")
    public ResponseEntity<ResponseDTO> SVC601(@RequestParam Map<String,Object> request, @RequestParam ("file") MultipartFile file){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0005", null);
    	return ResponseEntity.ok().body(iWAFileService.SVC601(request, file));
    }

}