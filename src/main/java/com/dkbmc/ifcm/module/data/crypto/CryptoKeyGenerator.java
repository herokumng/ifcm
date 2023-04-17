package com.dkbmc.ifcm.module.data.crypto;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.dkbmc.ifcm.module.core.ModuleConst;

public class CryptoKeyGenerator {
	public SecretKey generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(CryptoConst.DATA_ENCRYPTION_ALGORITHM);
		keyGenerator.init(CryptoConst.SECRET_KEY_LENGTH);
		SecretKey key = keyGenerator.generateKey();
		return key;
	}

	public SecretKey getKeyFromPassword(String password, String salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(CryptoConst.SECRET_SIGNATURE_ALGORITHM);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), CryptoConst.SECRET_ITORATION_COUNT,
				CryptoConst.SECRET_KEY_LENGTH);
		SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(),
				CryptoConst.DATA_ENCRYPTION_ALGORITHM);
		return secret;
	}

	public IvParameterSpec generateIv() {
		byte[] iv = new byte[CryptoConst.DATA_BLOCK_SIZE];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public IvParameterSpec getSecretIv(String saved_hex_key) throws UnsupportedEncodingException {
		return new IvParameterSpec(getSavedKey(saved_hex_key).substring(ModuleConst.MODULE_DIGIT_0, CryptoConst.DATA_BLOCK_SIZE)
				.getBytes(StandardCharsets.UTF_8));
	}

	public SecretKey loadToSavedKey(String saved_hex_key) {
		byte[] decodeKey = Base64.getDecoder().decode(getSavedKey(saved_hex_key).getBytes());
		return new SecretKeySpec(decodeKey, ModuleConst.MODULE_DIGIT_0, decodeKey.length,
				CryptoConst.DATA_ENCRYPTION_ALGORITHM);
	}

	private String getSavedKey(String saved_key) {
		return new String(hexToByteArray(saved_key), StandardCharsets.UTF_8);
	}

	// Create SHA-256 Encryption Password
	private String getEncrytPassword(String password) {
		String hex = null;

		try {
			byte[] bytes = new byte[CryptoConst.DATA_BLOCK_SIZE];
			SecureRandom random = SecureRandom.getInstance(CryptoConst.PASSWORD_RANDOM_ALGORITHM);
			random.nextBytes(bytes);

			//password + salt
			String saltPassword = password + new String(Base64.getEncoder().encode(bytes));
			MessageDigest messageDigest = MessageDigest.getInstance(CryptoConst.PASSWORD_ENCRYPTION_ALGORITHM);
			messageDigest.update(saltPassword.getBytes());
			hex = byteArrayToHex(messageDigest.digest());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return hex;
	}

	private String byteArrayToHex(byte[] encryption_key) {
		if (encryption_key == null || encryption_key.length == ModuleConst.MODULE_DIGIT_0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		for (byte keyByte : encryption_key) {
			buffer.append(String.format("%02x", keyByte));
		}
		return buffer.toString();
	}

	private byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() == ModuleConst.MODULE_DIGIT_0) {
			return null;
		}
		byte[] byteArray = new byte[hex.length() / ModuleConst.MODULE_DIGIT_2];
		for (int index = ModuleConst.MODULE_DIGIT_0; index < byteArray.length; index++) {
			byteArray[index] = (byte) Integer.parseInt(
					hex.substring(ModuleConst.MODULE_DIGIT_2 * index,
							ModuleConst.MODULE_DIGIT_2 * index + ModuleConst.MODULE_DIGIT_2),
					CryptoConst.DATA_BLOCK_SIZE);
		}
		return byteArray;
	}

}
