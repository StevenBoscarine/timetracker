package com.boscarine.finddup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * The Java wrappers needed to send command to OS, such as "locate" and "md5sum"
 * 
 * @author steven
 * 
 */
public class NativeCodeWrapper {

	// private static final Log logger =
	// LogFactory.getLog(NativeCodeWrapper.class);

	public static List<String> runNativeCommand(String... cmd) {
		final List<String> out = new ArrayList<String>();
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
