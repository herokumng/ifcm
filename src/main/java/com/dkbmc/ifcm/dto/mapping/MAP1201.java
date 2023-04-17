package com.dkbmc.ifcm.dto.mapping;

import java.time.LocalDateTime;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MAP1201 extends BaseTimeEntity {
	private String id;
	private String file_division;
	private String file_type;

	private String file_path;
	private String map_id;
	public MAP1201(String id, String file_division, String file_type, String file_path, String map_id, LocalDateTime create_datetime, LocalDateTime last_modified_datetime) {
		this.id = id;
		this.file_division = file_division;
		this.file_type = file_type;
		this.file_path = file_path;
		this.map_id = map_id;
		this.setCreated_datetime(create_datetime);
		this.setLast_modified_datetime(last_modified_datetime);
	}
}
