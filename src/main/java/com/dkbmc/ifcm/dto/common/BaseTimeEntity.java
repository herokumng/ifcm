package com.dkbmc.ifcm.dto.common;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseTimeEntity {
	private LocalDateTime created_datetime;
    private LocalDateTime last_modified_datetime;
}