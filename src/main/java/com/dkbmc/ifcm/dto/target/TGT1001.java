package com.dkbmc.ifcm.dto.target;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TGT1001 extends BaseTimeEntity{
    private String id;
    private String map_id;
    private String name;
    private String sys_code;

    public TGT1001(String id, String map_id, String name, String sys_code, LocalDateTime create_datetime, LocalDateTime last_modified_datetime) {
        this.id = id;
        this.map_id = map_id;
        this.name = name;
        this.sys_code = sys_code;
        this.setCreated_datetime(create_datetime);
        this.setLast_modified_datetime(last_modified_datetime);
    }

}
