package com.dkbmc.ifcm.dto.system;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SYS4001 extends BaseTimeEntity{
	private String id;
	private String code;
	private String name;
	private String system;
}
