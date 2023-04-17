package com.dkbmc.ifcm.controller.map;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.module.common.message.MessageHandler;
import com.dkbmc.ifcm.service.map.IWAFileService;
import com.dkbmc.ifcm.service.map.WA110FileService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.handler.logging.LogLevel;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Api(tags = { "Field List" })
@Controller
@RequestMapping("/map/wa110")
public class WA110 {
    private final IWAFileService iWAFileService;
    public WA110(WA110FileService wa110FileService){
        iWAFileService = wa110FileService;
    }

    @RequestMapping("/list")
    public String list() {
        return "Field_list";
    }

    /**
     * 맵핑 리스트에서 선택시 호출될 맵핑 정보
     * @param request (mapId) 매핑 호출에 필요한 id
     * @return 기본 맵핑 정보와 맵핑 필드 정보
     */
    @GetMapping("/svc101")
    public ResponseEntity <ResponseDTO> SVC101(@RequestParam Map<String,Object> request){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0006", null);
    	return ResponseEntity.ok().body(iWAFileService.SVC101(request));
    }

    /**
     * 매핑 등록 프로그램
     * @param request json 형식의 파라미터로
     *      { *업데이트 시    "mapId" : "",                                                         //맵핑 아이디 (map1001)
     *                      "basicInfo" : {                                                                                    // 맵핑 시스템 정보  map1001, map1002
     *                                 "cat_code" : "" ,                                          // 카테코리 코드
     *                                 "cat_name" : "" ,                                          // 카테고리 이름
     *                                 "name" : "",                                               // 맵핑 이름
     *                                 "description" : "",                                        // 맵핑 설명
     *                                 "src_sys_code" : "",                                       // 소스 시스템 코드
     *                                 "src_sys_name" : "",                                       // 소스 시스템 이름
     *                                 "src_name" : "",                                           // 소스 오브젝트 이름
     *                                 "tgt_sys_code" : "",                                       // 타겟 시스템 코드
     *                                 "tgt_sys_name" : "",                                       // 타겟 시스템 이름
     *                                 "tgt_obj_name" : "",                                       // 타겟 오브젝트 이름
     *                                 "map_creator" : ""                                         // 맵핑 생성자
     *                                 },
     *                      "fieldMappingList" : {
     *                                             "tgtField" : [                                                             // 타겟 필드 정보
     *                                                               {
     *                                     *업데이트 시                   "id" : "",                        // map1101 id
     *                                     *업데이트 시                   "map_id" : "",                    // map1001 id
     *                                     *업데이트 시                   "src_fld_id" : "",                // 소스 필드 아이디
     *                                                                  "src_fld_name" : "",              // 소스 필드 명
     *                                                                  "src_fld_name" : "",              // 소스 필드 명
     *                                                                  "src_fld_type" : "",              // 소스 필드 데이터 타입
     *                                                                  "src_fld_max_length" : "",        // 소스 필드 데이터 최대 길이
     *                                                                  "src_fld_nullable" : "",          // 소스 필드 데이터 null 여부
     *                                                                  "src_fld_precision" : "",         // 전체 자리 수
     *                                                                  "src_fld_digits" : ""             // 소숫점 이하 자리 수,
     *                                     *업데이트 시                   "tgt_fld_id" : "",                        // 타겟 필드 아이디
     *                                                                  "tgt_fld_name" : "",              // 타겟 필드 명
     *                                                                  "tgt_fld_type" : "",              // 타겟 필드 데이터 타입
     *                                                                  "tgt_fld_max_length" : "",        // 타겟 필드 데이터 최대 길이
     *                                                                  "tgt_fld_nullable" : "",          // 타겟 필드 데이터 null여부
     *                                                                  "tgt_fld_precision" : "",         // 전체 자리 수
     *                                                                  "tgt_fld_digits" : ""             // 소숫점 이하 자리수
     *                                                               }
     *                                                           ]
     *                                            }
     *                 }
     *
     * @return 성공여부
     */
    @PostMapping("/svc201")
    public ResponseEntity<ResponseDTO> SVC201(@RequestBody HashMap<String,Object> request){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0007", null);
    	return ResponseEntity.ok().body((iWAFileService.SVC201(request)));
    }

    /**
     * salesforce api object field meta data
     * @param request srcSysCode 소스 시스템 코드,tgtSysCode 타겟 시스템 코드,tgtObjName 타겟 오브젝트 이름
     * @return salesforce target object field;
     */
    @GetMapping("/svc601")
    public ResponseEntity<ResponseDTO> SVC601(@RequestParam Map<String,Object> request, @RequestParam (required = false, value = "file") MultipartFile file){
    	MessageHandler.getInstance().setModuleMessage(LogLevel.INFO.name(), "0008", null);
    	return ResponseEntity.ok().body(iWAFileService.SVC601(request, file));
    }


}
