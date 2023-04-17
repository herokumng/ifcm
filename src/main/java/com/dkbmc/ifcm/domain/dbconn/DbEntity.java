package com.dkbmc.ifcm.domain.dbconn;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * fileName     : DbEntity
 * author       : inayoon
 * date         : 2023-03-06
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-28       inayoon            최초생성
 */
@Entity
@Table(name = "dbinfo_tbl", schema = "test2")
@Getter
@NoArgsConstructor
public class DbEntity {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "DB_HOST")
    private String host;

    @Column(name = "DB_DRIVER")
    private String driver;

    @Column(name = "DB_PORT")
    private String port;

    @Column(name = "DB_USER")
    private String user;

    @Column(name = "DB_PW")
    private String pw;

    @Column(name = "DB_DATABASE")
    private String db;

    @Column(name = "CREATE_DATETIME")
    private LocalDateTime createdDatetime;

    @Column(name = "LAST_MODIFIED_DATETIME")
    private LocalDateTime lastModifiedDatetime;


    @Builder
    public DbEntity(String id, String dbHost, String dbDriver, String dbPort,
                        String dbUser, String dbPw, String dbDataBase,
                        LocalDateTime createdDatetime, LocalDateTime lastModifiedDatetime) {
        this.id = id;
        this.host = dbHost;
        this.driver = dbDriver;
        this.port = dbPort;
        this.user = dbUser;
        this.pw = dbPw;
        this.db = dbDataBase;
        this.createdDatetime = createdDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
    }
}
