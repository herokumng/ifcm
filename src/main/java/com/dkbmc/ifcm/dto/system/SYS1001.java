package com.dkbmc.ifcm.dto.system;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SYS1001 extends BaseTimeEntity{
	private String id;
	private String division;
	private String sub_div_code;
	private String type;
	private String directory;
	private String file_name;
	private String path;
	private String pack;
}
