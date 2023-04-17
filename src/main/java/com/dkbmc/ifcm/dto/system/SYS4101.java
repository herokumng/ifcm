package com.dkbmc.ifcm.dto.system;


import com.dkbmc.ifcm.dto.common.BaseTimeEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SYS4101 extends BaseTimeEntity{
	private String id;
	private String sys_id;
	private String type;
	private String domain;
	private String client_id;
	private String client_secret;
	private String grant_type;
	private String username;
	private String password;
	private String content_type;
	private String method;
	private String path;
}
