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
/**
 * This class uses UNIX or Cygwin commands md5sum and locate instead of Java equivalents
 * @author steven
 *
 */
public class FindDupsNativeEngine {
	private static final Log logger = LogFactory.getLog(FindDupsNativeEngine.class);
	private static final int NUMBER_OF_PARALLEL_MD5SUM_CALLS = 3;

	// private static final int NUMBER_OF_PARALLEL_MD5SUM_CALLS = 1;

	public List<FileEntry> createFileEntries(String locatePattern, FileEntryStore store) {
		long start = System.currentTimeMillis();
		Collection<String> files = NativeCodeWrapper.runNativeCommand("locate", locatePattern);
		logger.info("found " + files.size() + " candidates for pattern: " + locatePattern);
		return buildFileEntriesFromFileList(files, store);
	}

	private List<FileEntry> buildFileEntriesFromFileList(Collection<String> files, FileEntryStore store) {
		final List<FileEntry> out = new ArrayList<FileEntry>(files.size());
		final ExecutorService exec = Executors.newFixedThreadPool(NUMBER_OF_PARALLEL_MD5SUM_CALLS);
		final List<Future<FileEntry>> threadReferences = new ArrayList<Future<FileEntry>>();
		final List<File> filesReturnedByLocateCommand = createFileList(files, store);
		// using remove() instead of enhanced for loop to conserve RAM
		while (!filesReturnedByLocateCommand.isEmpty()) {
			File file = filesReturnedByLocateCommand.remove(0);
			// Don't send non-existent files returned by 'locate' through the system
			if (!file.exists()) {
				continue;
			}
			if (store.exists(file)) {
				// don't do expensive hash if hash has been completed.
				out.add(store.findByFileName(file.getAbsolutePath()));
				continue;
			}
			Callable<FileEntry> task = new MD5ThreadWrapper(file);
			threadReferences.add(exec.submit(task));
		}
		logger.info(threadReferences.size() + " requests submitted to thread pool for hashing.");
		// using remove() instead of enhanced for loop to conserve RAM
		while (!threadReferences.isEmpty()) {
			Future<FileEntry> task = threadReferences.remove(0);
			try {
				FileEntry fileEntry = task.get();
				if (fileEntry != null) {
					out.add(fileEntry);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		List<Runnable> shutdownNow = exec.shutdownNow();
		if (!shutdownNow.isEmpty()) {
			logger.warn("The following threads are still open:" + shutdownNow);
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
		logger.info("hashing time:  " + executionTime + " for a file of size " + (file.length() / 1024) + "k " + fileName);
		return fileEntry;
	}
}
