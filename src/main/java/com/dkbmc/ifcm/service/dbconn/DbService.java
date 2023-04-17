package com.dkbmc.ifcm.service.dbconn;

import com.dkbmc.ifcm.config.FileConfig;
import com.dkbmc.ifcm.domain.dbconn.DbEntity;
import com.dkbmc.ifcm.dto.dbcon.DbDto;
import com.dkbmc.ifcm.module.common.db.DBConnection;
import com.dkbmc.ifcm.config.QueryConfig;
import com.dkbmc.ifcm.dto.common.ResponseDTO;
import com.dkbmc.ifcm.repository.dbconn.DbRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * fileName     : DbService
 * author       : inayoon
 * date         : 2023-02-23
 * description  : DBConnection 테스트 Service
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-23       inayoon            최초생성
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DbService {
    private final DbRepository dbRepository;

    private static DbService instance = null;
    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private QueryConfig queryConfig;
    private FileConfig fileCon;

    /**
     * DB 연결 테스트
     * @param  request
     * @return ResponseDTO
     */
    public ResponseDTO getDbConnTest(HashMap<String, String> request) {
        DBConnection pool = DBConnection.getInstance();
        boolean isConnected = false;
        try {
            conn = pool.getConnection(request);

            if(conn == null || conn.isClosed()) {
                isConnected = false;
            } else {
                isConnected = true;
            }

        } catch(Exception e) {
            log.error("DbConnTest Exception:: {}", e.getMessage());
            isConnected = false;
        } finally {
            if(pool != null)
                pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(isConnected) {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }

    /**
     * DB 입력정보 Table 저장
     * @param  request
     * @return ResponseDTO
     */
    public ResponseDTO setDbInfoSave(HashMap<String, String> request) {
        DBConnection pool = DBConnection.getInstance();
        boolean isConnected = false;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime now = LocalDateTime.parse(LocalDateTime.now().format(formatter));

        try {
            conn = pool.getConnection(request);

            if(conn == null || conn.isClosed()) {
                isConnected = false;
            } else {
                isConnected = true;

                String DB_DRIVER = request.get("driver");
                String DB_HOST = request.get("host");
                String DB_PORT = request.get("port");
                String DB = request.get("db");
                String DB_USER = request.get("user");
                String DB_PW = request.get("pw");
                String DB_id = request.get("id");

                DbDto dbDto = new DbDto();
                dbDto.setId(DB_id);
                dbDto.setHost(DB_HOST);
                dbDto.setDriver(DB_DRIVER);
                dbDto.setPort(DB_PORT);
                dbDto.setUser(DB_USER);
                dbDto.setPw(DB_PW);
                dbDto.setDb(DB);
                dbDto.setCreatedDatetime(now);
                dbDto.setLastModifiedDatetime(now);

                DbEntity dbEntity = makeDbEntity(dbDto);
                dbRepository.save(dbEntity);
            }
        } catch(Exception e) {
            log.error("DbInfoSave Exception: {}", e.getMessage());
            isConnected = false;
        } finally {
            if(pool != null)
                pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(isConnected) {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }

    /**
     * DB 정보 파일 저장
     * @param  request
     * @return ResponseDTO
     */
    public ResponseDTO setDbConnFileSave(HashMap<String, String> request) {
        DBConnection pool = DBConnection.getInstance();
        boolean isConnected = false;
        try {
            conn = pool.getConnection(request);

            if(conn == null || conn.isClosed()) {
                isConnected = false;
            } else {
                isConnected = true;

                // DB 접속정보 파일 생성
                fileCon = new FileConfig();
                fileCon.createFile(request);
            }
        } catch(Exception e) {
            log.error("DbConnFileSave Exception:: {}", e.getMessage());
            isConnected = false;
        } finally {
            if(pool != null)
                pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(isConnected) {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult(isConnected);
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }


    /**
     * 연동시스템명 리스트 조회
     * @param
     * @return ResponseDTO
     */
    public ResponseDTO getSysTypeList() {
        List<String> sysTypeList = new ArrayList<String>();
        try {
            fileCon = new FileConfig();
            sysTypeList = fileCon.getSysTypeList();

        } catch(Exception e) {
            log.error("SysTypeList Exception:: {}", e.getMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(sysTypeList.size() > 0) {
            responseDTO.setResult(sysTypeList);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }

    /**
     * 연동시스템 DB접속정보 조회
     * @param  systemType
     * @return ResponseDTO
     */
    public ResponseDTO getSysTypeData(String systemType) {
        JSONObject sysTypeData = new JSONObject();
        try {
            fileCon = new FileConfig();
            sysTypeData = fileCon.getSysTypeData(systemType);

        } catch(Exception e) {
            log.error("SysTypeData Exception:: {}", e.getMessage());
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(sysTypeData.size() > 0) {
            responseDTO.setResult(sysTypeData);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Fail!");
        }
        return responseDTO;
    }



    /**
     * 테이블 조회
     * @param  request
     * @return ResponseDTO
     */
    public ResponseDTO getDbTblList(HashMap<String, String> request) {
        DBConnection pool = DBConnection.getInstance();
        List<HashMap<String, String>> tblList = new ArrayList<HashMap<String, String>>();

        try {
            conn = pool.getConnection(request);
            stmt = conn.createStatement();

            queryConfig = new QueryConfig();
            String tblSchema = queryConfig.getTblSchema(request);
            rs = stmt.executeQuery(tblSchema);

            while(rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("tblName", rs.getString("TABLE_NAME"));
                tblList.add(map);
            }
        } catch(Exception e) {
            log.error("DbTblList Exception: {}", e.getMessage());
        } finally {
            pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(conn != null) {
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
     * MetaData 조회
     * @param  request
     * @return ResponseDTO
     */
    public ResponseDTO getDbTblMeta(HashMap<String, String> request) throws SQLException {
        DBConnection pool = DBConnection.getInstance();
        List<HashMap<String, String>> colList = new ArrayList<HashMap<String, String>>();

        try {
            conn = pool.getConnection(request);
            stmt = conn.createStatement();

            queryConfig = new QueryConfig();
            String colName = queryConfig.getColName(request);
            rs = stmt.executeQuery(colName);

            while(rs.next()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("tblSchema", rs.getString("TABLE_SCHEMA"));
                map.put("tblName", rs.getString("TABLE_NAME"));
                map.put("colName", rs.getString("COLUMN_NAME"));
                map.put("dataType", rs.getString("DATA_TYPE"));
                map.put("dataLength", rs.getString("CHARACTER_MAXIMUM_LENGTH"));
                map.put("colComment", rs.getString("COLUMN_COMMENT"));
                map.put("colKey", rs.getString("COLUMN_KEY"));
                map.put("colNotNull", rs.getString("IS_NULLABLE"));
                colList.add(map);
            }

        } catch(Exception e) {
            log.error("DbTblMeta Exception: {}", e.getMessage());
        } finally {
            pool.freeClose(conn, stmt, rs);
        }

        ResponseDTO responseDTO = new ResponseDTO();
        if(conn != null) {
            responseDTO.setResult(colList);
            responseDTO.setReturn_msg("SUCCESS");
        } else {
            responseDTO.setResult("");
            responseDTO.setReturn_msg("FAIL");
            responseDTO.setError_code("500");
            responseDTO.setError_msg("Connection Timeout!");
        }
        return responseDTO;
    }



    public DbEntity makeDbEntity(DbDto dbDto){
        DbEntity dbEntity = DbEntity.builder()
                .id(dbDto.getId())
                .dbHost(dbDto.getHost())
                .dbDriver(dbDto.getDriver())
                .dbPort(dbDto.getPort())
                .dbUser(dbDto.getUser())
                .dbPw(dbDto.getPw())
                .dbDataBase(dbDto.getDb())
                .createdDatetime(dbDto.getCreatedDatetime())
                .lastModifiedDatetime(dbDto.getLastModifiedDatetime())
                .build();
        return dbEntity;
    }

}
