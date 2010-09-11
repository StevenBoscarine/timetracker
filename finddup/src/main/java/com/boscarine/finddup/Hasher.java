package com.boscarine.finddup;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Methods to generate MD5 file hashes
 * 
 * @author steven
 */
public final class Hasher {
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(Hasher.class);

	public static final String createMd5HashInJava(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return DigestUtils.md5Hex(fis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String createMd5HashNatively(File file) {
		return NativeCodeWrapper.runNativeCommand("md5sum", file.getAbsolutePath()).get(0).substring(0, 32);
	}
}