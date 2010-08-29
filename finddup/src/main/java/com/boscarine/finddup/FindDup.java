package com.boscarine.finddup;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.enterprise.context.ApplicationScoped;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@ApplicationScoped
public class FindDup {
	private FileEntryStore store=FileEntryStore.getInstance();
	final Find find = new Find();

	private static Map<String, List<FileEntry>> findAllDuplicates(List<FileEntry> filesFound, final FileEntryStore store) {
		final Map<String, List<FileEntry>> out = new TreeMap<String, List<FileEntry>>();
		for (FileEntry entry : filesFound) {
			String md5 = entry.getMd5();
			List<FileEntry> duplicates = store.findByMD5(md5);
//			purgeMissingFiles(duplicates);
			if (duplicates.size() > 1) {
				out.put(md5, duplicates);
			}
		}
		return out;
	}

//	private static void purgeMissingFiles(List<FileEntry> duplicates){
//		for(Iterator<FileEntry> iterator = duplicates.iterator(); iterator.hasNext(); ){
//			FileEntry entry = iterator.next();
//			File file = new File(entry.getFileName());
//			if(!file.exists()){
//				logger.error(file.getAbsoluteFile() + " does not exist.  Was it moved or deleted?");
//				iterator.remove();
//			}
//		}
//	}
//	
	
	private static final Log logger = LogFactory.getLog(FindDup.class);

	public  Map<String, List<FileEntry>> findDuplicates(String extension) {
		long start = System.currentTimeMillis();
		List<FileEntry> entries = find.createFileEntries(extension, store);
//		for (FileEntry entry : entries) {
//			store.persist(entry);
//		}
		Map<String, List<FileEntry>> dups = findAllDuplicates(entries, store);
		logger.info("completed in " + (System.currentTimeMillis() - start) + " ms");
		return dups;
	}

    public void shutdown(){
        System.out.println("Beginning proper shutdown");
        store.shutdown();
    }
}
