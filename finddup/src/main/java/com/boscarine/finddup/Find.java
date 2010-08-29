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

public class Find {
	private final Log logger = LogFactory.getLog(getClass());

	public List<FileEntry> createFileEntries(String locatePattern, FileEntryStore store) {
		Collection<String> files = NativeCodeWrapper.runNativeCommand("locate", locatePattern);
		return buildFileEntriesFromFileList(files, store);
	}

	private List<FileEntry> buildFileEntriesFromFileList(Collection<String> files, FileEntryStore store) {
		final ExecutorService exec = Executors.newFixedThreadPool(3);
		List<FileEntry> out = new ArrayList<FileEntry>(files.size());
		logger.info("\n\n\n preparing to process " + files.size() + " files");
		List<Future<EntryJobOutput>> workers = new ArrayList<Future<EntryJobOutput>>();

		for (File file : createFileList(files, store)) {
			Callable<EntryJobOutput> task = new MD5ThreadWrapper(file);
			workers.add(exec.submit(task));
		}
		for (Future<EntryJobOutput> task : workers) {
			try {
				EntryJobOutput wrapper = task.get();
				FileEntry fileEntry = wrapper.entry;
				if (fileEntry != null) {
					if (wrapper.file.exists()) {
						out.add(fileEntry);
						store.persist(fileEntry);
					} else {
						logger.warn(wrapper.file.getAbsolutePath()+ " not found");
					}
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
			if (store.exists(file)) {
				logger.info("found " + fileName + " in db");
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
	private static class MD5ThreadWrapper implements Callable<EntryJobOutput> {
		private final File file;

		private static final Log logger = LogFactory.getLog(MD5ThreadWrapper.class);

		public MD5ThreadWrapper(File file) {
			this.file = file;
		}

		/**
		 * Create our object representation of a file and MD5 hash
		 */
		@Override
		public EntryJobOutput call() throws Exception {
			final long initializationTime = System.currentTimeMillis();
			final String md5native = Hasher.createMd5HashNatively(file);
			final long executionTime = System.currentTimeMillis() - initializationTime;
			final String fileName = file.getAbsolutePath();
			final FileEntry fileEntry = new FileEntry(fileName, file.lastModified(), file.length(), md5native);
			String statusMessage = "hashing time:  " + executionTime + " for a file of size " + (file.length() / 1024) + "k " + fileName;
			logger.info(statusMessage);
			return new EntryJobOutput(fileEntry, file, statusMessage);
		}

	}

	/**
	 * Allows us to return status to UI.
	 */
	private static class EntryJobOutput {
		protected final FileEntry entry;
		// for convenience
		protected final File file;

		public EntryJobOutput(FileEntry entry, File file, String statusMessage) {
			super();
			this.entry = entry;
			this.file = file;
			this.statusMessage = statusMessage;
		}

		protected final String statusMessage;
	}
}
