package com.boscarine.finddup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Hasher {

	private static final Log logger = LogFactory.getLog(Hasher.class);

	public static String createMd5HashInJava(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			return DigestUtils.md5Hex(fis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String createMd5HashNatively(File file) {
		return NativeCodeWrapper.runNativeCommand("md5sum", file.getAbsolutePath()).get(0).substring(0, 32);

	}


}
