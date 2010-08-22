package com.boscarine.finddup;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Find {

	private static final ExecutorService exec = Executors.newFixedThreadPool(3);

	public List<FileEntry> createFileEntries(String locatePattern) {
		Collection<String> files = runUNIXCommand("locate", locatePattern);
		List<FileEntry> out = new ArrayList<FileEntry>(files.size());
		List<Future<FileEntry>> workers = new ArrayList<Future<FileEntry>>();
		for (String fileName : files) {
			Callable<FileEntry> task = new FileEntryWorker(fileName);
			workers.add(exec.submit(task));
		}
		for (Future<FileEntry> task : workers) {
			try {
				FileEntry fileEntry = task.get();
				if (fileEntry != null) {
					out.add(fileEntry);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return out;
	}

	public static List<String> runUNIXCommand(String... cmd) {
		List<String> out = new ArrayList<String>();

		try {
			Process p = Runtime.getRuntime().exec(cmd);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

			for (String s = null; (s = stdInput.readLine()) != null;) {
				out.add(new String(s));
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return out;
	}

	public static class FileEntryWorker implements Callable<FileEntry> {
		private final String fileName;

		private final Log logger = LogFactory.getLog(getClass());

		public FileEntryWorker(String fileName) {
			this.fileName = fileName;
		}

		@Override
		public FileEntry call() throws Exception {

			File file = new File(fileName);
			if (file.isDirectory()) {
				logger.warn(fileName + " is a directory");
				return null;
			}
			long before = System.currentTimeMillis();
			String md5native = Hasher.createMd5HashNatively(file);
			long after = System.currentTimeMillis();

			FileEntry fileEntry = new FileEntry(fileName, file.lastModified(), file.length(), md5native);
			long timeInUnix = after - before;
			System.out.println("hashing time:  " + timeInUnix + " for a file of size " + (file.length() / 1024) + "k " + fileName);

			return fileEntry;
		}

	}

}
