package com.dkbmc.ifcm.service.dbconn;

import com.dkbmc.ifcm.config.QueryConfig;
import com.dkbmc.ifcm.domain.dbconn.DbEntity;
import com.dkbmc.ifcm.domain.dbconn.MapIdEntity;
import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.dto.dbcon.MapIdDto;
import com.dkbmc.ifcm.module.api.rest.SfApiConfig;
import com.dkbmc.ifcm.module.common.db.DBConnection;
import com.dkbmc.ifcm.repository.dbconn.DbRepository;
import com.dkbmc.ifcm.repository.dbconn.MapIdRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * fileName     : MapIdService
 * author       : inayoon
 * date         : 2023-03-28
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-28       inayoon            최초생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MapIdService {
    private final DbRepository dbRepository;
    private final MapIdRepository mapIdRepository;

    private static MapIdService instance = null;

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private QueryConfig queryConfig;
    private ResultSetMetaData rsmd;

    private final SfApiConfig sfApiConfig;

    // Object MetaData
    @Value("${apex_call_url}")
    private String APEX_CALL_URL;

    // Object Data
    @Value("${apex_call_url2}")
    private String APEX_CALL_URL2;

    @Value("${company_number}")
    private String COMPANY_NUMBER;


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
    public ResponseDTO getSfObjMeta(String objName)  {
        List<HashMap<String, Object>> sfMetaList = new ArrayList<HashMap<String, Object>>();
        List<HashMap<String, Object>> sfFieldList = new ArrayList<HashMap<String, Object>>();
        boolean isResult = false;
        JSONObject jsonObject = new JSONObject();

        try {
            // MetaData
            isResult = requestApiMetaData(objName, sfMetaList);

            HashMap<String, Object> map = sfMetaList.get(0);
            sfFieldList = (List<HashMap<String, Object>>) map.get("fields");

//            List<HashMap<String, Object>> filterFields = sfFieldList.stream()
//                    .filter(f -> (boolean) f.get("nillable") || f.get("name").equals("Name"))
//                    .collect(Collectors.toList());
//            jsonObject.put("meta", filterFields);

            jsonObject.put("meta", sfFieldList);
            isResult = true;

        } catch (Exception e) {
            log.error("SfObjMeta ERROR: {}", e.getMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if (isResult) {
            responseDTO.setResult(jsonObject);
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
     * Legacy Table 조회
     * @param
     * @return ResponseDTO
     */
    public ResponseDTO getLgcTbl() {
        DBConnection pool = DBConnection.getInstance();
        boolean isConnected = false;
        List<String> tblList = new ArrayList<String>();

        try {
            HashMap<String, String> dbInfo = getDbInfo(COMPANY_NUMBER);

            conn = pool.getConnection(dbInfo);
            if(conn != null) {
                stmt = conn.createStatement();

                queryConfig = new QueryConfig();
                String tblSchema = queryConfig.getTblSchema(dbInfo);
                rs = stmt.executeQuery(tblSchema);

                while(rs.next()) {
                    tblList.add(rs.getString("TABLE_NAME"));
                }
                isConnected = true;
            }

        } catch(Exception e) {
            log.error("getDbTbl ERROR:: {}", e.getMessage());
            isConnected = false;

        } finally {
            if(pool != null)
                pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(isConnected) {
            responseDTO.setResult(tblList);
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
     * Legacy Table - Meta 및 Data 조회
     * @param
     * @return ResponseDTO
     */
    public ResponseDTO getLgcTblData(String tblName) throws SQLException {
        DBConnection pool = DBConnection.getInstance();
        List<HashMap<String, String>> tblMetaList = new ArrayList<HashMap<String, String>>();
        List<HashMap<String, String>> tblDataList = new ArrayList<HashMap<String, String>>();
        JSONObject jsonObject = new JSONObject();
        boolean isResult = false;

        try {
            HashMap<String, String> dbInfo = getDbInfo(COMPANY_NUMBER);

            conn = pool.getConnection(dbInfo);
            stmt = conn.createStatement();

            queryConfig = new QueryConfig();
            // MetaData
            String tblMetaData = queryConfig.getColName2(dbInfo, tblName);
            rs = stmt.executeQuery(tblMetaData);
            rsmd =  rs.getMetaData();
            int columns = rsmd.getColumnCount();

            while(rs.next()) {
                HashMap<String,String> metaMap = new HashMap<>();
                for (int i=1; i<=columns; i++) {
                    String metaName = rsmd.getColumnName(i);
                    String metaValue = rs.getString(metaName);
                    metaMap.put(metaName, metaValue);
                }
                tblMetaList.add(metaMap);
            }

            // Data
            String tblData = queryConfig.getTblData(dbInfo, tblName);
            rs = stmt.executeQuery(tblData);
            rsmd =  rs.getMetaData();
            int cols = rsmd.getColumnCount();

            while (rs.next()) {
                HashMap<String,String> map = new HashMap<String,String>();
                for (int i=1; i<=cols; i++) {
                    String colName = rsmd.getColumnName(i);
                    String value = rs.getString(colName);
                    map.put(colName, value);
                }
                tblDataList.add(map);
            }

            jsonObject.put("meta", tblMetaList);
            jsonObject.put("data", tblDataList);
            isResult = true;

        } catch (Exception e) {
            log.error("LgcTblMeta ERROR: {}", e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if ( conn != null ) conn.close();
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if (isResult) {
            responseDTO.setResult(jsonObject);
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
     * MappingIdTable - MappingData 저장
     * @param
     * @return ResponseDTO
     */
    public ResponseDTO setMappingData(List<MapIdDto> mapdata) {
        DBConnection pool = DBConnection.getInstance();
        boolean isConnected = false;
        List<MapIdDto> mapIdAddDtoList = new ArrayList<>();

        try {
            HashMap<String, String> dbInfo = getDbInfo(COMPANY_NUMBER);

            conn = pool.getConnection(dbInfo);
            if(conn == null || conn.isClosed()) {
                isConnected = false;

            } else {
                isConnected = true;

                List<MapIdEntity> mappingAddList = mapdata.stream()
                        .map(this::makeMapIdEntity)
                        .collect(Collectors.toList());

                List<MapIdEntity> savedMapIdAdd = mapIdRepository.saveAll(mappingAddList);
                mapIdAddDtoList = savedMapIdAdd.stream()
                        .map(this::makeMapIdDto)
                        .collect(Collectors.toList());
            }

        } catch(Exception e) {
            log.error("MappingDataSave ERROR: {}", e.getMessage());
            isConnected = false;

        } finally {
            if(pool != null)
                pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(isConnected) {
            responseDTO.setResult(mapIdAddDtoList);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }



    /****************************/
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


    /**
     * 해당 key로 DB 정보조회
     * @param  dbKey
     * @return HashMap
     */
    public HashMap<String,String> getDbInfo(String dbKey) {
        DbEntity allById = dbRepository.findAllById(dbKey);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        HashMap<String,String> hashMap = objectMapper.convertValue(allById, HashMap.class);
        return hashMap;
    }


    /**
     * makeMapIdEntity
     * @param  mapIdDto
     * @return MapIdEntity
     */
    public MapIdEntity makeMapIdEntity(MapIdDto mapIdDto){
        String name = "";
        MapIdEntity mapIdEntity = MapIdEntity.builder()
                .name(name)
                .sourceObj(mapIdDto.getSourceObj())
                .targetTbl(mapIdDto.getTargetTbl())
                .sourceFld(mapIdDto.getSourceFld())
                .sourceLabel(mapIdDto.getSourceLabel())
                .targetFld(mapIdDto.getTargetFld())
                .targetLabel(mapIdDto.getTargetLabel())
                .build();
        return mapIdEntity;
    }
    /**
     * makeMapIdDto
     * @param  mapIdEntity
     * @return MapIdDto
     */
    public MapIdDto makeMapIdDto(MapIdEntity mapIdEntity){
        MapIdDto mapIdDto = new MapIdDto();
        mapIdDto.setName(mapIdEntity.getName());
        mapIdDto.setSourceObj(mapIdEntity.getSourceObj());
        mapIdDto.setSourceFld(mapIdEntity.getSourceFld());
        mapIdDto.setSourceLabel(mapIdEntity.getSourceLabel());
        mapIdDto.setTargetTbl(mapIdEntity.getTargetTbl());
        mapIdDto.setTargetFld(mapIdEntity.getTargetFld());
        mapIdDto.setTargetLabel(mapIdEntity.getTargetLabel());
        return mapIdDto;
    }

}