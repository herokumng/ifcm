package com.dkbmc.ifcm.dto.system;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SYS3002 extends BaseTimeEntity{
	private String id;
	private String classname;
	private String method;
	private String type;
	private String action;
}
