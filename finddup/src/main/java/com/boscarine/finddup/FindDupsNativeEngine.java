package com.boscarine.finddup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FindDupsNativeEngine {
	private static final Log logger = LogFactory.getLog(FindDupsNativeEngine.class);
	// private static final int NUMBER_OF_PARALLEL_MD5SUM_CALLS = 3;
	private static final int NUMBER_OF_PARALLEL_MD5SUM_CALLS = 1;

	public List<FileEntry> createFileEntries(String locatePattern, FileEntryStore store) {
		Collection<String> files = NativeCodeWrapper.runNativeCommand("locate", locatePattern);
		logger.info("found " + files.size() + " candidates.");
		return buildFileEntriesFromFileList(files, store);
	}

	private List<FileEntry> buildFileEntriesFromFileList(Collection<String> files, FileEntryStore store) {

		final List<FileEntry> out = new ArrayList<FileEntry>(files.size());
		if (NUMBER_OF_PARALLEL_MD5SUM_CALLS == 1) {
			for (File file : createFileList(files, store)) {
				//don't do expensive hash if hash has been completed.  Don't send non-existant files returned by 'locate' through the system
				if (store.exists(file) || !file.exists()) {
					continue;
				}
				out.add(computeMD5AndCreateTransferObject(file));

			}
		} else {
			final ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_MD5SUM_CALLS);
			final List<Future<FileEntry>> threadReferences = new ArrayList<Future<FileEntry>>();
			for (File file : createFileList(files, store)) {
				if (store.exists(file) || !file.exists()) {
					out.add(store.findByFileName(file.getAbsolutePath()));
					continue;
				}
				Callable<FileEntry> task = new MD5ThreadWrapper(file);
				threadReferences.add(exec.submit(task));
			}
			for (Future<FileEntry> task : threadReferences) {
				try {
					FileEntry fileEntry = task.get();
					if (fileEntry != null) {
						out.add(fileEntry);
					}
				} catch (Exception e) {
					logger.error(e);
					throw new RuntimeException(e);
				}
			}
			logger.warn("The following threads are still open:" + exec.shutdownNow());
		}

		return out;
	}

	private List<File> createFileList(Collection<String> files, FileEntryStore store) {
		List<File> out = new ArrayList<File>(files.size());
		if (files.size() == 0) {
			return out;
		}
		for (String fileName : files) {
			File file = new File(fileName);
			if (file.isDirectory()) {
				logger.warn(fileName + " is a directory");
				continue;
			}

			out.add(file);
		}
		return out;
	}

	/**
	 * Simple thread wrapper
	 * 
	 * @author steven
	 * 
	 */
	private static class MD5ThreadWrapper implements Callable<FileEntry> {
		private final File file;

		public MD5ThreadWrapper(File file) {
			this.file = file;
		}

		/**
		 * Create our object representation of a file and MD5 hash
		 */
		@Override
		public FileEntry call() throws Exception {
			return computeMD5AndCreateTransferObject(file);
		}
	}

	private static FileEntry computeMD5AndCreateTransferObject(File file) {
		final long initializationTime = System.currentTimeMillis();
		final String md5checksum = Hasher.createMd5HashNatively(file);
		final long executionTime = System.currentTimeMillis() - initializationTime;
		final String fileName = file.getAbsolutePath();
		final FileEntry fileEntry = new FileEntry(fileName, file.lastModified(), file.length(), md5checksum);
		logger.info("hashing time:  " + executionTime + " for a file of size " + (file.length() / 1024) + "k "
				+ fileName);
		return fileEntry;
	}
}
