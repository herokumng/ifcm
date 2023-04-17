package com.dkbmc.ifcm.dto.target;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TGT1101 extends BaseTimeEntity{

    private String id;
    private String tgt_id;
    private String name;
    private String type;

    private int max_length;

    private boolean nullable;

    private int precision;
    private int digits;

    public TGT1101(String id, String tgt_id, String name, String type, int max_length, boolean nullable, int precision, int digits , LocalDateTime create_datetime, LocalDateTime last_modified_datetime) {
        this.id = id;
        this.tgt_id = tgt_id;
        this.name = name;
        this.type = type;
        this.max_length = max_length;
        this.nullable = nullable;
        this.precision = precision;
        this.digits = digits;
        this.setCreated_datetime(create_datetime);
        this.setLast_modified_datetime(last_modified_datetime);
    }
}
