package com.dkbmc.ifcm.module.common.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;

/**
 * fileName     : DBConnection
 * author       : inayoon
 * date         : 2023-02-23
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-23       inayoon            최초생성
 */
@Slf4j
public class DBConnection {
    private static DBConnection instance = null;

    private DBConnection() {
    }

    /**
     * Singleton Pattern
     * @param
     * @return
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized(DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }


    /**
     * Connection
     * @param  request
     * @return Connection
     */
    public Connection getConnection(HashMap<String, String> request) {
        Connection conn = null;
        String DB_DRIVER = request.get("driver");
        String DB_HOST = request.get("host");
        String DB_PORT = request.get("port");
        String DB = request.get("db");
        String DB_USER = request.get("user");
        String DB_PW = request.get("pw");

        try {
            String DB_TYPE = dbTypeCheck(DB_DRIVER);
            String DB_URL = "jdbc:"+DB_TYPE+"://"+DB_HOST+":"+DB_PORT+"/"+DB;
            Class.forName(DB_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PW);
            if (conn != null) {
                log.info("===> DB CONNECT SUCCESS");
//                log.info("DB_DRIVER: {}", DB_DRIVER);
//                log.info("DB_URL:    {}", DB_URL);
//                log.info("DB_USER:   {},  DB_PW:   {}", DB_USER, DB_PW);
            }
        }catch (ClassNotFoundException e) {
            log.error("DB_DRIVER ERR:: {}", e.getMessage());
        }catch (SQLException e) {
            log.error("DB_CONNECT ERR:: {}", e.getMessage());
        }
        return conn;
    }

    /**
     * Database Type Check
     * @param  DB_DRIVER
     * @return String
     */
    public String dbTypeCheck(String DB_DRIVER) {
        if(DB_DRIVER.contains("mariadb")){
            return "mariadb";
        } else if (DB_DRIVER.contains("mysql")) {
            return "mysql";
        } else if (DB_DRIVER.contains("oracle")) {
            return "oracle";
        }
        return "";
    }


    /**
     * DisConnection
     * @params Connection, Statement, PreparedStatement, ResultSet
     * @return
     */
    public void freeClose(Connection c, PreparedStatement p, ResultSet r) {
        try {
            if (r != null)
                r.close();
            if (p != null)
                p.close();
            freeClose(c);
        } catch (SQLException e) {
            log.error("freeClose(c, p, r) ERR:: {}", e.getMessage());
        }
    }

    public void freeClose(Connection c, Statement s, ResultSet r) {
        try {
            if (r != null)
                r.close();
            if (s != null)
                s.close();
            freeClose(c);
        } catch (SQLException e) {
            log.error("freeClose(c, s, r) ERR:: {}", e.getMessage());
        }
    }

    public void freeClose(Connection c, PreparedStatement p) {
        try {
            if (p != null)
                p.close();
            freeClose(c);
        } catch (SQLException e) {
            log.error("freeClose(c, p) ERR:: {}", e.getMessage());
        }
    }

    public void freeClose(Connection c, Statement s) {
        try {
            if (s != null)
                s.close();
            freeClose(c);
        } catch (SQLException e) {
            log.error("freeClose(c, s) ERR:: {}", e.getMessage());
        }
    }

    public void freeClose(Connection c) {
        try {
            if (c != null)
                c.close();
        } catch (SQLException e) {
            log.error("freeClose(c) ERR:: {}", e.getMessage());
        }
    }

}
