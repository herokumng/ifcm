package com.dkbmc.ifcm.domain.dbconn;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * fileName     : MapIdEntity
 * author       : inayoon
 * date         : 2023-03-28
 * description  :
 * =======================================================
 * DATE             AUTHOR              NOTE
 * -------------------------------------------------------
 * 2023-03-28      inayoon            최초생성
 */
@Entity
@Table(name = "mappingid_tbl", schema = "test2")
@Getter
@NoArgsConstructor
public class MapIdEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "source_obj")
    private String sourceObj;

    @Column(name = "target_tbl")
    private String targetTbl;

    @Column(name = "source_fld")
    private String sourceFld;

    @Column(name = "source_label")
    private String sourceLabel;

    @Column(name = "target_fld")
    private String targetFld;

    @Column(name = "target_label")
    private String targetLabel;


    @Builder
    public MapIdEntity(Integer id, String name, String sourceObj, String targetTbl,
                    String sourceFld, String sourceLabel, String targetFld, String targetLabel) {
        this.id = id;
        this.name = name;
        this.sourceObj = sourceObj;
        this.targetTbl = targetTbl;
        this.sourceFld = sourceFld;
        this.sourceLabel = sourceLabel;
        this.targetFld = targetFld;
        this.targetLabel = targetLabel;
    }

}