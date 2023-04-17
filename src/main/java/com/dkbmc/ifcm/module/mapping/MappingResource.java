package com.dkbmc.ifcm.module.mapping;

import java.util.List;

import com.dkbmc.ifcm.dto.mapping.MAP1001;
import com.dkbmc.ifcm.dto.mapping.MAP1002;
import com.dkbmc.ifcm.dto.mapping.MAP1201;
import com.dkbmc.ifcm.dto.mapping.MAP1301;

import lombok.Data;

@Data
public class MappingResource {
	// Module Mapping Resource
	private List<MAP1001> map1001;	// 맵핑 정의
	private List<MAP1002> map1002;	// 맵핑 목록

	private List<MAP1201> map1201;	// 맵핑 별 업로드 다운로드 파일 관리
	private List<MAP1301> map1301;	// 맵핑 별 업무 분류 관리

	public void clearResource() {
		map1001.clear();
		map1002.clear();
		map1201.clear();
		map1301.clear();
	}
}
