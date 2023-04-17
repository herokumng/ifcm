package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1002 extends BaseTimeEntity{
	private String id;
	private String cat_code;
	private String cat_name;
	private String name;
	private String description;
	private String src_id;
	private String src_sys_code;
	private String src_sys_name;
	private String src_obj_name;
	private String tgt_id;
	private String tgt_sys_code;
	private String tgt_sys_name;
	private String tgt_obj_name;
	private String map_creator;

	public MAP1002(String id, String cat_code, String cat_name, String name, String description, String src_id, String src_sys_code, String src_sys_name, String src_obj_name, String tgt_id, String tgt_sys_code, String tgt_sys_name, String tgt_obj_name, String map_creator, LocalDateTime create_datetime,LocalDateTime last_modified_datetime)
	{
		this.id = id;
		this.cat_code = cat_code;
		this.cat_name = cat_name;
		this.name = name;
		this.description = description;
		this.src_id = src_id;
		this.src_sys_code = src_sys_code;
		this.src_sys_name = src_sys_name;
		this.src_obj_name = src_obj_name;
		this.tgt_id = tgt_id;
		this.tgt_sys_code = tgt_sys_code;
		this.tgt_sys_name = tgt_sys_name;
		this.tgt_obj_name = tgt_obj_name;
		this.map_creator = map_creator;
		this.setCreated_datetime(create_datetime);
		this.setLast_modified_datetime(last_modified_datetime);
	}
}
