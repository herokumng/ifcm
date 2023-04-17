package com.dkbmc.ifcm.dto.category;

import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CAT1001 extends BaseTimeEntity{
	private String id;
	private String sys_code;
	private String code;
	private String name;
}