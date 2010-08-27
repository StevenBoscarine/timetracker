package com.boscarine.finddup;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FindDup {

	private static Map<String, List<FileEntry>> findDuplicates(final FileEntryStore store) {
		final Map<String, List<FileEntry>> out = new TreeMap<String, List<FileEntry>>();
		for (FileEntry entry : store.listAll()) {
			String md5 = entry.getMd5();
			List<FileEntry> duplicates = store.findByMD5(md5);
			if (duplicates.size() > 1) {
				out.put(md5, duplicates);
			}
		}
		return out;
	}

	private static final Log logger = LogFactory.getLog(FindDup.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		findDuplicates("*.java");
		findDuplicates("*.mov");
		findDuplicates("*.avi");
	}

	private static void findDuplicates(String extension) {
		final Find find = new Find();
		final FileEntryStore store = new FileEntryStore();
		long start = System.currentTimeMillis();
		Collection<FileEntry> entries = find.createFileEntries(extension, store);
		for (FileEntry entry : entries) {
			store.persist(entry);
		}
		Map<String, List<FileEntry>> dups = findDuplicates(store);
		logger.info("duplicates:" + printMap(dups));
		store.shutdown();

		logger.info("completed in " + (System.currentTimeMillis() - start) + " ms");
	}

	public static String printMap(Map<?, ?> c) {
		StringBuilder sb = new StringBuilder();
		for (Object key : c.keySet()) {
			sb.append("\n" + key + "\t" + c.get(key));
		}
		return sb.toString();
	}

}
