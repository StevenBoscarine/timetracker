package com.boscarine.finddup;

import java.io.File;
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
	private final Log logger = LogFactory.getLog(getClass());


	public List<FileEntry> createFileEntries(String locatePattern, FileEntryStore store) {
		Collection<String> files = NativeCodeWrapper.runNativeCommand("locate", locatePattern);
		return buildFileEntriesFromFileList(files, store);
	}

	private List<FileEntry> buildFileEntriesFromFileList(Collection<String> files, FileEntryStore store) {
		 final ExecutorService exec = Executors.newFixedThreadPool(3);
		List<FileEntry> out = new ArrayList<FileEntry>(files.size());
		List<Future<FileEntry>> workers = new ArrayList<Future<FileEntry>>();
		
		for (File file : createFileList(files, store )) {
			Callable<FileEntry> task = new MD5ThreadWrapper(file);
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
		logger.warn(exec.shutdownNow());
		return out;
	}

	private List<File> createFileList(Collection<String> files, FileEntryStore store) {
		List<File> out = new ArrayList<File>(files.size());
		for (String fileName : files) {
			File file = new File(fileName);
			if (file.isDirectory()) {
				logger.warn(fileName + " is a directory");
				continue;
			}
			if(store.exists(file)){
				continue;
			}
			out.add(file);
		}
		return out;
	}

	private static class MD5ThreadWrapper implements Callable<FileEntry> {
		private final File file;

		private final Log logger = LogFactory.getLog(getClass());

		public MD5ThreadWrapper(File file) {
			this.file = file;
		}

		@Override
		public FileEntry call() throws Exception {
			long before = System.currentTimeMillis();
			String md5native = Hasher.createMd5HashNatively(file);
			long after = System.currentTimeMillis();

			String fileName = file.getAbsolutePath();
			FileEntry fileEntry = new FileEntry(fileName, file.lastModified(), file.length(), md5native);
			long timeInUnix = after - before;
			logger.info("hashing time:  " + timeInUnix + " for a file of size " + (file.length() / 1024) + "k " + fileName);

			return fileEntry;
		}
	}
}
