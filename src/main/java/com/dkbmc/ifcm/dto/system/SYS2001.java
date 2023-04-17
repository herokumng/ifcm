package com.dkbmc.ifcm.dto.system;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SYS2001 {
	private String id;
	private LocalDateTime date_time;
	private String session;
	private String client;
	private String creator;
	private String type;
	private String action;
	private String msg_type;
	private String msg_code;
	private Object message;
	private String path_detail;

	public String getSavingForm() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
				getId(), getDate_time().toString(), getSession(), getClient(),getCreator(),
				getPath_detail(), getType(), getAction(),
				getMsg_type(), getMsg_code(), String.valueOf(getMessage()));
	}

	public String getLoggingForm() {
		return String.format("%s-%s : [%s-%s-%s] [%s] [%s] :: %s",
				getDate_time().toString(), getPath_detail(), getSession(), getClient(),
				getCreator(), getMsg_type(), getMsg_code(), String.valueOf(getMessage()));
	}
}
