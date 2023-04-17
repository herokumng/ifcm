package com.dkbmc.ifcm.dto.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MetaWrapper<T> {
	public MetaWrapper() {
		// TODO Auto-generated constructor stub
	}
	private String entityName;
	private List<T> dataList;
}
