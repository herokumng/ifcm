package com.dkbmc.ifcm.module.common.utils;

import java.time.Duration;
import java.time.Instant;

public class SystemChecker {
	private Instant startTime;
	private Instant finishTime;
	private long startMemory;
	private long finishMemory;

	private SystemChecker() {
	}

	private static class SystemCheckerHelper {
		private static final SystemChecker systemCheck = new SystemChecker();
	}

	public static SystemChecker getInstance() {
		return SystemCheckerHelper.systemCheck;
	}

	public void startTime() {
		startTime = Instant.now();
	}

	public void finishTime() {
		finishTime = Instant.now();
	}

	public String elapsedTime() {
		String elapsedTime = String.format("execute time : %d ms", Duration.between(startTime, finishTime).toMillis());
		startTime = null;
		finishTime = null;
		return elapsedTime;
	}

	public void startMemory() {
		startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}

	public void finishMemory() {
		finishMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
	}
	
	public String currentMemoryUsage() {
		return String.format("current used heap memory : %d MB", (Runtime.getRuntime().totalMemory()) / 1024 / 1024);
	}
	
	public String memoryUsage() {
		// 애플리케이션에 할당돈 힙메모리 사이즈. 이 사이즈를 넘어서면 OOM발생
		String memoryUsage = String.format("process used memory : %d MB",
				(finishMemory-startMemory) / 1024 / 1024);
		startMemory = 0;
		finishMemory = 0;
		return memoryUsage;
	}
}