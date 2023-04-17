package com.dkbmc.ifcm.module.core;

import java.util.List;

import com.dkbmc.ifcm.dto.category.CAT1001;
import com.dkbmc.ifcm.dto.log.LOG1001;
import com.dkbmc.ifcm.dto.system.SYS1001;
import com.dkbmc.ifcm.dto.system.SYS2001;
import com.dkbmc.ifcm.dto.system.SYS3001;
import com.dkbmc.ifcm.dto.system.SYS3002;
import com.dkbmc.ifcm.dto.system.SYS4001;

import lombok.Data;

@Data
public class ModuleResource {
	// Module System Resource
	private List<SYS1001> sys1001;	// 시스템 파일 경로
	private List<SYS3001> sys3001;	// 타입별 코드/출력 메시지
	private List<SYS3002> sys3002;  // 서비스 타입/액션
	private List<SYS4001> sys4001;	// 소스/타겟 사용 시스템
	private List<CAT1001> cat1001;	// 사용 시스템 업무 분류 코드 맵핑

	private SYS2001 sys2001;			// 로그 양식
	private LOG1001 log1001;			// 로그 파일 관리

	public void clearResource() {
		sys1001.clear();
		sys3001.clear();
		sys3002.clear();
		sys4001.clear();
		cat1001.clear();

		sys2001 = null;
		log1001 = null;
	}
}