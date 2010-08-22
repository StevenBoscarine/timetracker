package com.boscarine.finddup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class NativeCodeWrapper {

	private static final Log logger = LogFactory.getLog(NativeCodeWrapper.class);

	// public static List<String> runNativeCommand(String... cmd) {
	// List<String> out = new ArrayList<String>();
	//
	// try {
	// Process p = Runtime.getRuntime().exec(cmd);
	// BufferedReader stdInput = new BufferedReader(new
	// InputStreamReader(p.getInputStream()));
	//
	// for (String s = null; (s = stdInput.readLine()) != null;) {
	// out.add(new String(s));
	// }
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// return out;
	// }

	public static List<String> runNativeCommand(String... cmd) {
		List<String> out = new ArrayList<String>();
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			for (String s = null; (s = stdInput.readLine()) != null;) {
				out.add(new String(s));
			}
			for (String s = null; (s = stdErr.readLine()) != null;) {
				System.err.println(s);
			}
			stdInput.close();
			stdErr.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (p != null) {
				p.destroy();
			}
		}
		return out;
	}
}
