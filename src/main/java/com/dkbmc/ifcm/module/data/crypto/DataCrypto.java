package com.dkbmc.ifcm.module.data.crypto;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class DataCrypto {
	// Text
	public String encryptText(String plain_text, SecretKey key, IvParameterSpec iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(CryptoConst.DATA_USING_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(plain_text.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(cipherText);
	}

	// Text
	public String decryptText(String cipher_text, SecretKey key, IvParameterSpec iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(CryptoConst.DATA_USING_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipher_text));
		return new String(plainText);
	}

	// Object
	public SealedObject encryptObject(Serializable object, SecretKey key, IvParameterSpec iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, IOException, IllegalBlockSizeException {

		Cipher cipher = Cipher.getInstance(CryptoConst.DATA_USING_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		SealedObject sealedObject = new SealedObject(object, cipher);
		return sealedObject;
	}

	// Object
	public Serializable decryptObject(SealedObject sealed_object, SecretKey key,
			IvParameterSpec iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
			InvalidKeyException, ClassNotFoundException, BadPaddingException, IllegalBlockSizeException, IOException {

		Cipher cipher = Cipher.getInstance(CryptoConst.DATA_USING_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		Serializable unsealObject = (Serializable) sealed_object.getObject(cipher);
		return unsealObject;
	}
}
