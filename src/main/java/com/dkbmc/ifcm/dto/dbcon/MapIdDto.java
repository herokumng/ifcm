package com.dkbmc.ifcm.dto.dbcon;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * fileName     : MapIdDto
 * author       : inayoon
 * date         : 2023-04-07
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-04-07       inayoon            최초생성
 */
@Getter
@Setter
@NoArgsConstructor
public class MapIdDto {
    private Integer id;
    private String name;
    private String sourceObj;
    private String targetTbl;
    private String sourceFld;
    private String sourceLabel;
    private String targetFld;
    private String targetLabel;
}