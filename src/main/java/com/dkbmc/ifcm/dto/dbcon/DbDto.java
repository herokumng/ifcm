package com.dkbmc.ifcm.dto.dbcon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * fileName     : DbDto
 * author       : inayoon
 * date         : 2023-02-28
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-02-28       inayoon            최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class DbDto {
    private String id;
    private String host;
    private String driver;
    private String port;
    private String user;
    private String pw;
    private String db;
    private LocalDateTime createdDatetime;
    private LocalDateTime lastModifiedDatetime;
}