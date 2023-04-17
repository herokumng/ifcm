package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1001 extends BaseTimeEntity{
	private String id;
	private String cat_code;
	private String name;
	private String description;
	private String src_id;
	private String tgt_id;
	private String creator;

	public MAP1001(String id, String cat_code, String name, String description, String src_id, String tgt_id, String creator, LocalDateTime create_datetime,LocalDateTime last_modified_datetime) {
		this.id = id;
		this.cat_code = cat_code;
		this.name = name;
		this.description = description;
		this.src_id = src_id;
		this.tgt_id = tgt_id;
		this.creator = creator;
		this.setCreated_datetime(create_datetime);
		this.setLast_modified_datetime(last_modified_datetime);
	}
}
