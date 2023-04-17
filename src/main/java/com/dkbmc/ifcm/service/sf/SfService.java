package com.dkbmc.ifcm.service.sf;

import com.dkbmc.ifcm.module.api.rest.SfApiConfig;
import com.dkbmc.ifcm.dto.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * fileName     : SfService
 * author       : inayoon
 * date         : 2023-03-15
 * description  : Salesforce 테스트 Service
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-15       inayoon            최초생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SfService {

    private final SfApiConfig sfApiConfig;

    @Value("${apex_call_url}")
    private String APEX_CALL_URL;

    @Value("${apex_call_url2}")
    private String APEX_CALL_URL2;

    /**
     * SF ObjectName 조회
     * @param
     * @return ResponseDTO
     */
    public ResponseDTO getSfObjName() {
        List<String> standardObj = new ArrayList<>();
        standardObj.add("Employee");
        standardObj.add("User");
        standardObj.add("Account");
        standardObj.add("Product2");
        standardObj.add("ProductItem");
        standardObj.add("Opportunity");
        standardObj.add("Contract");
        standardObj.add("Contact");

        List<HashMap<String, Object>> objList = new ArrayList<HashMap<String, Object>>();
        List<String> objNameList = new ArrayList<>();
        boolean isResult = false;
        try {
            isResult = requestApiObjName(objList);

            for(HashMap<String, Object> map : objList) {
                List<HashMap<String, Object>> sObjects = (List<HashMap<String, Object>>) map.get("sobjects");
                for(HashMap<String, Object> map2 : sObjects) {
                    String name = (String) map2.get("name");
                    if(standardObj.contains(name)) {
                        objNameList.add(name);
                    }
                }
            }

        } catch (Exception e) {
            log.error("SfObjName ERROR: {}", e.getMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if (isResult) {
            responseDTO.setResult(objNameList);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Timeout!");
        }
        return responseDTO;
    }

    /**
     * SF Object - MetaData 조회
     * @param  objName
     * @return ResponseDTO
     */
    public ResponseDTO getSfObjMeta(String objName) {
        List<HashMap<String, Object>> metaList = new ArrayList<HashMap<String, Object>>();
        boolean isResult = false;
        try {
            isResult = requestApiMetaData(objName, metaList);

        } catch (Exception e) {
            log.error("SfObjMeta ERROR: {}", e.getMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if (isResult) {
            responseDTO.setResult(metaList);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Timeout!");
        }
        return responseDTO;
    }



    /**
     * ObjectName API 호출
     * @param  objList
     * @return boolean
     */
    public boolean requestApiObjName(List<HashMap<String, Object>> objList) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject resultCallApi = sfApiConfig.callApi(jsonObject, APEX_CALL_URL, HttpMethod.GET);
            objList.add(resultCallApi);
            return true;
        }catch (Exception e){
            log.error("ApiObjName ERROR: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Object MetaData API 호출
     * @param  objName
     * @return boolean
     */
    public boolean requestApiMetaData(String objName, List<HashMap<String, Object>> metaList) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject resultCallApi = sfApiConfig.callApi(jsonObject, APEX_CALL_URL+"/"+objName+"/describe", HttpMethod.GET);
            metaList.add(resultCallApi);
            return true;
        }catch (Exception e){
            log.error("ApiMetaData ERROR: {}", e.getMessage());
            return false;
        }
    }


}
