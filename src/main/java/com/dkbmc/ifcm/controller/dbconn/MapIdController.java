package com.dkbmc.ifcm.controller.dbconn;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.dto.dbcon.MapIdDto;
import com.dkbmc.ifcm.service.dbconn.MapIdService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

/**
 * fileName     : MapIdController
 * author       : inayoon
 * date         : 2023-03-28
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-28       inayoon            최초생성
 */
@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class MapIdController {

    private final MapIdService mapIdService;

    /**
     * SF ObjectName 조회
     * @param
     * @return
     */
    @GetMapping("/sf/objname")
    public ResponseEntity<ResponseDTO> getSfObjName() {
        return ResponseEntity.ok().body(mapIdService.getSfObjName());
    }
    /**
     * SF Object - MetaData 조회
     * @param  objName
     * @return
     */
    @GetMapping("/sf/{objName}")
    public ResponseEntity<ResponseDTO> getSfObjMeta(@PathVariable("objName") String objName) {
        return ResponseEntity.ok().body(mapIdService.getSfObjMeta(objName));
    }


    /**
     * Legacy Table 조회
     * @param
     * @return
     */
    @PostMapping("/lgc/tblname")
    public ResponseEntity<ResponseDTO> getLgcTbl() {
        return ResponseEntity.ok().body(mapIdService.getLgcTbl());
    }
    /**
     * Legacy Table - MetaData 조회
     * @param
     * @return
     */
    @PostMapping("/lgc/{tblName}")
    public ResponseEntity<ResponseDTO> getLgcTblData(@PathVariable("tblName") String tblName) throws SQLException {
        return ResponseEntity.ok().body(mapIdService.getLgcTblData(tblName));
    }


    /**
     * MappingIdTable - MappingData 저장
     * @param
     * @return
     */
    @PostMapping("/map/mapdata")
    public ResponseEntity<ResponseDTO> setMappingData(@RequestBody List<MapIdDto> mapdata) {
        return ResponseEntity.ok().body(mapIdService.setMappingData(mapdata));
    }

}