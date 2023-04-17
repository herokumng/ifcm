package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1301 extends BaseTimeEntity{
	private String id;
	private String sys_code;
	private String cat_code;
	private String cat_name;
	private String map_id;

	public MAP1301(String id, String sys_code, String cat_code, String cat_name, String map_id, LocalDateTime create_datetime, LocalDateTime last_modified_datetime) {
		this.id = id;
		this.sys_code = sys_code;
		this.cat_code = cat_code;
		this.cat_name = cat_name;
		this.map_id = map_id;
		this.setCreated_datetime(create_datetime);
		this.setLast_modified_datetime(last_modified_datetime);
	}
}
