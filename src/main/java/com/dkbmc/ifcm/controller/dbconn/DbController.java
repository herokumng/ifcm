package com.dkbmc.ifcm.controller.dbconn;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.service.dbconn.DbService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * fileName     : DbController
 * author       : inayoon
 * date         : 2023-02-23
 * description  : DBConnection 테스트 Controller
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-23       inayoon            최초생성
 */
@Slf4j
@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class DbController {
    private final DbService dbService;

    @GetMapping({"/", ""})
    public String main() {
        return "dbconn/dbconnect";
    }

    /**
     * DB 연결 테스트
     * @param  request
     * @return
     */
    @PostMapping("/dbtest")
    public ResponseEntity<ResponseDTO> getDbConnTest(@RequestBody HashMap<String, String> request) {
        return ResponseEntity.ok().body(dbService.getDbConnTest(request));
    }

    /**
     * DB 입력정보 Table 저장
     * @param  request
     * @return
     */
    @PostMapping("/lgc/dbsave")
    public ResponseEntity<ResponseDTO> setDbInfoSave(@RequestBody HashMap<String, String> request) {
        return ResponseEntity.ok().body(dbService.setDbInfoSave(request));
    }

    /**
     * DB 정보 파일 저장
     * @param  request
     * @return
     */
    @PostMapping("/lgc/filesave")
    public ResponseEntity<ResponseDTO> setDbConnSave(@RequestBody HashMap<String, String> request) {
        return ResponseEntity.ok().body(dbService.setDbConnFileSave(request));
    }


    /**
     * 연동시스템명 리스트 조회
     * @param
     * @return
     */
//    @GetMapping("/dbsys")
//    public ResponseEntity<ResponseDTO> getSysTypeList() {
//        return ResponseEntity.ok().body(dbService.getSysTypeList());
//    }
    /**
     * 연동시스템 DB접속정보 조회
     * @param
     * @return
     */
//    @GetMapping("/dbsys/{systemType}")
//    public ResponseEntity<ResponseDTO> getSysTypeData(@PathVariable("systemType") String systemType) {
//        return ResponseEntity.ok().body(dbService.getSysTypeData(systemType));
//    }



    /**
     * 테이블 조회
     * @param  request
     * @return
     */
    @PostMapping("/dbtbl")
    public ResponseEntity<ResponseDTO> getDbTblList(@RequestBody HashMap<String, String> request) {
        return ResponseEntity.ok().body(dbService.getDbTblList(request));
    }

    /**
     * MetaData 조회
     * @param  request
     * @return
     */
    @PostMapping("/dbmeta")
    public ResponseEntity<ResponseDTO> getDbTblMeta(@RequestBody HashMap<String, String> request) throws SQLException {
        return ResponseEntity.ok().body(dbService.getDbTblMeta(request));
    }

}
