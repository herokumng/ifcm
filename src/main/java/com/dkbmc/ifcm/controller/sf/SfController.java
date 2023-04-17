package com.dkbmc.ifcm.controller.sf;

import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.service.sf.SfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * fileName     : SfController
 * author       : inayoon
 * date         : 2023-03-15
 * description  : Salesforce 테스트 Controller
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-15       inayoon            최초생성
 */
@Slf4j
@Controller
@RequestMapping("/api/sf")
@RequiredArgsConstructor
public class SfController {

    private final SfService sfService;

    @GetMapping({"/", ""})
    public String main() {
        return "sf/sfBeginner";
    }

    /**
     * SF ObjectName 조회
     * @param
     * @return
     */
    @GetMapping("/objnames")
    public ResponseEntity<ResponseDTO> getSfObjName() {
        return ResponseEntity.ok().body(sfService.getSfObjName());
    }

    /**
     * SF Object - MetaData 조회
     * @param  objName
     * @return
     */
    @GetMapping("/obj/{objName}")
    public ResponseEntity<ResponseDTO> getSfObjMeta(@PathVariable("objName") String objName) {
        return ResponseEntity.ok().body(sfService.getSfObjMeta(objName));
    }

}
