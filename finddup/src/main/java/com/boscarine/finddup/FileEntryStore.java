package com.boscarine.finddup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityIndex;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.SecondaryIndex;

public class FileEntryStore {
	private final MyDbEnv env = new MyDbEnv();
	private final Log logger = LogFactory.getLog(getClass());
	private final EntityStore store;
	private final PrimaryIndex<String, FileEntry> primaryIndex;
	private final SecondaryIndex<String, String, FileEntry> secondaryIndex;

	public FileEntryStore() {
		File envHome = new File(System.getProperty("user.home"), ".finddup");
		envHome.mkdirs();
		logger.info(envHome.getAbsoluteFile());
		env.setup(envHome);
		store = env.getEntityStore();
		primaryIndex = store.getPrimaryIndex(String.class, FileEntry.class);
		secondaryIndex = store.getSecondaryIndex(primaryIndex, String.class, "md5");
	}

	public void persist(FileEntry entity) {
		primaryIndex.put(entity);
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

	public List<FileEntry> findByMD5(String md5) {
		EntityIndex<String, FileEntry> subIndex = secondaryIndex.subIndex(md5);
		EntityCursor<FileEntry> cursor = subIndex.entities();
		return retrieveAllMembersFromIndex(cursor);
	}

	private List<FileEntry> retrieveAllMembersFromIndex(EntityCursor<FileEntry> pi_cursor) {
		List<FileEntry> out = new ArrayList<FileEntry>();
		try {
			for (FileEntry entry : pi_cursor) {
				out.add(entry);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			pi_cursor.close();

		}
		return out;
	}

	public void shutdown() {
		store.close();
		env.close();
	}
}
