package com.dkbmc.ifcm.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;

/**
 * fileName     : QueryConfig
 * author       : inayoon
 * date         : 2023-02-23
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-23       inayoon            최초생성
 */
@Slf4j
@NoArgsConstructor
public class QueryConfig {
    private String sql;
    private String db;
    private String tbl;


    /**
     * 테이블 스키마 조회
     * @param  request
     * @return String
     */
    public String getTblSchema(HashMap<String, String> request) {
        db = request.get("db");

        sql = "SELECT * FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_SCHEMA='"+ db +"'";
        return sql;
    }

    /**
     * metaData 조회
     * @param  request
     * @return String
     */
    public String getColName(HashMap<String, String> request) {
        db = request.get("db");
        tbl = request.get("tbl");

        sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA='"+ db +"'AND TABLE_NAME='"+ tbl +"'";
        return sql;
    }

    public String getColName2(HashMap<String, String> request, String tbl) {
        db = request.get("db");

        sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_SCHEMA='"+ db +"'AND TABLE_NAME='"+ tbl +"'";
        return sql;
    }

    /**
     * tableData 조회
     * @param  request, tbl
     * @return String
     */
    public String getTblData(HashMap<String, String> request, String tbl) {
        db = request.get("db");

        sql = "SELECT * FROM " + db +"."+ tbl;
        return sql;
    }

}