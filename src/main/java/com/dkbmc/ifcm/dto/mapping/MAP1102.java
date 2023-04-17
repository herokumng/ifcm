package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1102 extends BaseTimeEntity{
	private String id;
	private String map_id;
	private int order;
	private String src_fld_id;
	private String src_fld_name;
	private String src_fld_type;
	private int src_fld_max_length;
	private boolean src_fld_nullable;
	private int src_fld_precision;
	private int src_fld_digits;
	private String tgt_fld_id;
	private String tgt_fld_name;

	private String tgt_fld_type;
	private int tgt_fld_max_length;
	private boolean tgt_fld_nullable;
	private int tgt_fld_precision;
	private int tgt_fld_digits;
	public MAP1102(String id, String map_id, int order, String src_fld_id, String src_fld_name, String src_fld_type, int src_fld_max_length, boolean src_fld_nullable, int src_fld_precision, int src_fld_digits, String tgt_fld_id, String tgt_fld_name, String tgt_fld_type, int tgt_fld_max_length, boolean tgt_fld_nullable, int tgt_fld_precision, int tgt_fld_digits, LocalDateTime create_date_time,LocalDateTime last_modified_datetime) {
		this.id = id;
		this.map_id = map_id;
		this.order = order;
		this.src_fld_id = src_fld_id;
		this.src_fld_name = src_fld_name;
		this.src_fld_type = src_fld_type;
		this.src_fld_max_length = src_fld_max_length;
		this.src_fld_nullable = src_fld_nullable;
		this.src_fld_precision = src_fld_precision;
		this.src_fld_digits = src_fld_digits;
		this.tgt_fld_id = tgt_fld_id;
		this.tgt_fld_name = tgt_fld_name;
		this.tgt_fld_type = tgt_fld_type;
		this.tgt_fld_max_length = tgt_fld_max_length;
		this.tgt_fld_nullable = tgt_fld_nullable;
		this.tgt_fld_precision = tgt_fld_precision;
		this.tgt_fld_digits = tgt_fld_digits;
		this.setCreated_datetime(create_date_time);
		this.setLast_modified_datetime(last_modified_datetime);
	}

}
