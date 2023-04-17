package com.dkbmc.ifcm.module.data.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CryptoKeyStore {

	private static String savedHexKey;

	private CryptoKeyStore() {
		readKeyStoreFile();
	}

	private static class CryptoKeyStoreHelper {
		private static final CryptoKeyStore keyStore = new CryptoKeyStore();
	}

	public static CryptoKeyStore getInstance() {
		return CryptoKeyStoreHelper.keyStore;
	}

	public String getSavedHexKey() {
        return savedHexKey;
    }

	private void readKeyStoreFile() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					new FileInputStream(new File(CryptoConst.SAVED_KEY_FILE)), StandardCharsets.UTF_8));
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				// line = cryptoUtil.dataDecryption(line);
				builder.append(line);
			}
			savedHexKey = builder.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
