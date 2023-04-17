package com.dkbmc.ifcm.module.common.utils;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import com.dkbmc.ifcm.module.data.crypto.CryptoKeyGenerator;
import com.dkbmc.ifcm.module.data.crypto.CryptoKeyStore;
import com.dkbmc.ifcm.module.data.crypto.CryptoTypeDef.CryptoProcessType;
import com.dkbmc.ifcm.module.data.crypto.DataCrypto;

public class CryptoUtils {
	private DataCrypto crypto;
	private CryptoKeyGenerator keygen;
	private SecretKey key;
	private IvParameterSpec iv;

	public CryptoUtils(CryptoProcessType crypto_type) {
		crypto = new DataCrypto();
		keygen = new CryptoKeyGenerator();
		getSecretKey(crypto_type);
	}

	public void getSecretKey(CryptoProcessType crypto_type) {
		// 데이터 저장 및 로드 시에는 등록된 키를 사용
		try {
			if (crypto_type.equals(CryptoProcessType.FILE_DATA) || crypto_type.equals(CryptoProcessType.DATA_BASE)) {
				String savedHexKey = CryptoKeyStore.getInstance().getSavedHexKey();
				key = keygen.loadToSavedKey(savedHexKey);
				iv = keygen.getSecretIv(savedHexKey);

			}
			// 데이터 전송 간에는 password 인증으로 키 생성 후 키 교환 예정, 추후 개발 예정
			if (crypto_type.equals(CryptoProcessType.DATA_TRANS)) {
				key = keygen.getKeyFromPassword("ifcmcommonmoduleproject", "platformsi");
				// key = keygen.generateKey();
				iv = keygen.generateIv();
			}
		} catch (UnsupportedEncodingException | NoSuchAlgorithmException | InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Processing for plain text line
	public String dataEncryption(String plain_text) {
		String cipherText = null;
		try {
			cipherText = crypto.encryptText(plain_text, key, iv);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherText;
	}

	// Processing for plain text line
	public String dataDecryption(String cipher_text) {
		String plainText = null;
		try {
			plainText = crypto.decryptText(cipher_text, key, iv);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | BadPaddingException | IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plainText;
	}

	// Processing for Json Object
	public SealedObject objectEncryption(Serializable plain_object) {
		SealedObject cipherObject = null;
		try {
			cipherObject = crypto.encryptObject(plain_object, key, iv);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cipherObject;
	}

	// Processing for Json Object
	public Serializable objectDecryption(SealedObject cipher_object) {
		Serializable plainObject = null;
		try {
			plainObject = crypto.decryptObject(cipher_object, key, iv);
		} catch (InvalidKeyException | NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidAlgorithmParameterException | ClassNotFoundException | BadPaddingException
				| IllegalBlockSizeException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return plainObject;
	}
}
