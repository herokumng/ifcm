package com.dkbmc.ifcm.module.mapping;

import java.util.List;

import com.dkbmc.ifcm.dto.mapping.MAP1101;
import com.dkbmc.ifcm.dto.mapping.MAP1102;
import com.dkbmc.ifcm.dto.source.SRC1001;
import com.dkbmc.ifcm.dto.source.SRC1101;
import com.dkbmc.ifcm.dto.system.SYS4101;
import com.dkbmc.ifcm.dto.target.TGT1001;
import com.dkbmc.ifcm.dto.target.TGT1101;

import lombok.Data;

@Data
public class FieldMappingResource {
	private List<SYS4101> sys4101;	// 시스템 접속 정보

	// load by mapping_id
	private List<MAP1101> map1101;	// 필드 맵핑
	private List<MAP1102> map1102;	// 필드 맵핑 목록

	private List<SRC1001> src1001;	// 소스 기본 정보
	private List<SRC1101> src1101;	// 소스 필드 정보

	private List<TGT1001> tgt1001;	// 타겟 기본 정보
	private List<TGT1101> tgt1101;	// 타겟 필드 정보

	public void clearResource() {
		sys4101.clear();
		map1101.clear();
		map1102.clear();
		src1001.clear();
		src1101.clear();
		tgt1001.clear();
		tgt1101.clear();
	}
}
