package com.dkbmc.ifcm.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.dkbmc.ifcm.module.mapping.MappingProcess;

@Component
public class ModuleApplicationRunner implements ApplicationRunner{

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub
		MappingProcess process = MappingProcess.getInstance();
	}

}
