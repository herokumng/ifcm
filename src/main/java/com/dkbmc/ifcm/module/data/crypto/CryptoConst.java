package com.dkbmc.ifcm.module.data.crypto;

public final class CryptoConst {
	// Using For Module Crypto Setting
	protected final static String PASSWORD_ENCRYPTION_ALGORITHM = "SHA-256";
	protected final static String PASSWORD_RANDOM_ALGORITHM = "SHA1PRNG";

	protected final static String SECRET_SIGNATURE_ALGORITHM = "PBKDF2WithHmacSHA256";
	protected final static int SECRET_ITORATION_COUNT = 65536;
	protected final static int SECRET_KEY_LENGTH = 256;

	protected final static int DATA_BLOCK_SIZE = 16;
	protected final static String DATA_ENCRYPTION_ALGORITHM = "AES";
	protected final static String DATA_USING_ALGORITHM = "AES/CBC/PKCS5Padding";

	protected final static String SAVED_KEY_FILE = "config/KeyStore";
}
