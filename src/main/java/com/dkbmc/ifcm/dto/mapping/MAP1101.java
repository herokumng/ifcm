package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1101 extends BaseTimeEntity{
	private String id;
	private String map_id;
	private int order;
	private String src_fld_id;

	private String tgt_fld_id;

	public MAP1101(String id, String map_id, int order, String src_fld_id, String tgt_fld_id, LocalDateTime created_datetime, LocalDateTime last_modified_datetime) {
		this.id = id;
		this.map_id = map_id;
		this.order = order;
		this.src_fld_id = src_fld_id;
		this.tgt_fld_id = tgt_fld_id;
		this.setCreated_datetime(created_datetime);
		this.setLast_modified_datetime(last_modified_datetime);
	}
}
