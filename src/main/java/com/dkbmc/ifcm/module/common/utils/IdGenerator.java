package com.dkbmc.ifcm.module.common.utils;

/**
 *
 * 식별자 생성 - 26자리 ULID 생성, Singleton 적용.
 * 사용 시 아래와 같이 호출 :
 * 		String Id = IdGenerator.getInstance().getId()
 * @author DY
 *
 */
public class IdGenerator {
	private static String Id;

	private IdGenerator() {}

	private static class IdGeneratorHelper {
		private static final IdGenerator idGenerator = new IdGenerator();
	}

	public static IdGenerator getInstance() {
		Id = new KeyGenerator().nextULID();
		return IdGeneratorHelper.idGenerator;
	}

	public String getId() {
        return Id;
    }
}
