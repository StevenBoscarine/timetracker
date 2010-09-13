package com.boscarine.finddup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PreDestroy;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sleepycat.je.Transaction;
import com.sleepycat.je.TransactionConfig;
import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

/**
 * Wrapper for BDB code
 * 
 * @author steven
 * 
 */
@Singleton
@Named
public class FileEntryStore {

	private final BDBWrapper env = new BDBWrapper();
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(getClass());
	private final EntityStore store;
	private final PrimaryIndex<String, FileEntry> primaryIndex;
	private final SecondaryIndex<String, String, FileEntry> secondaryIndex;

	private FileEntryStore() {

		env.setup();
		store = env.getEntityStore();
		primaryIndex = store.getPrimaryIndex(String.class, FileEntry.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex, String.class, "md5");
	}

	public void persist(FileEntry entity) {
		primaryIndex.putNoReturn(entity);
		store.sync();
	}

	public boolean exists(File file) {
		FileEntry fileEntry = primaryIndex.get(file.getAbsolutePath());
		if (fileEntry != null && fileEntry.getSize() == file.length() && fileEntry.getLastModified() == file.lastModified()) {
			return true;
		}
		return false;
	}

	public List<FileEntry> listAll() {
		return retrieveAllMembersFromIndex(primaryIndex.entities());
	}

	public FileEntry findByFileName(String fileName) {
		return primaryIndex.get(fileName);
	}

	public List<FileEntry> findByMD5(String md5) {
		EntityIndex<String, FileEntry> subIndex = secondaryIndex.subIndex(md5);
		EntityCursor<FileEntry> cursor = subIndex.entities();
		return retrieveAllMembersFromIndex(cursor);
	}

	private List<FileEntry> retrieveAllMembersFromIndex(EntityCursor<FileEntry> cursor) {
		List<FileEntry> out = new ArrayList<FileEntry>();
		try {
			for (FileEntry entry : cursor) {
				out.add(entry);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			cursor.close();
		}
		return out;
	}

	public void deleteOrphanedFiles() {
		List<String> filesToDelete = new ArrayList<String>();
		EntityCursor<FileEntry> cursor = primaryIndex.entities();
		long counter = 0;
		try {
			for (FileEntry entry : cursor) {
				File file = new File(entry.getFileName());
				if (!file.exists()) {
					System.out.println(entry.getFileName() + " is missing!");
					filesToDelete.add(file.getAbsolutePath());
				}
				counter++;
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			cursor.close();
		}
		System.out.println(counter + " files");
		System.out.println(filesToDelete.size() + " files to be cleared from DB.");
		try {
			Transaction tx = env.createTransaction();
			for (String fileToDelete : filesToDelete) {
				// primaryIndex.delete(tx, fileToDelete);
				primaryIndex.delete(fileToDelete);
			}
			tx.commit();
			env.cleanDB();
			Thread.sleep(50000);
		} catch (Exception e) {
			shutdown();
		}

	}

	@PreDestroy
	public void shutdown() {
		store.close();
		env.close();
	}
}
