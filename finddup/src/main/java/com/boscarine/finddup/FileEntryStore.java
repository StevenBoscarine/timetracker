package com.boscarine.finddup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class FileEntryStore {
	private final MyDbEnv env = new MyDbEnv();
	private final Log logger = LogFactory.getLog(getClass());
	private final EntityStore store;
	private final PrimaryIndex<String, FileEntry> primaryIndex;

	public FileEntryStore() {
		File envHome = new File(System.getProperty("user.home"), ".finddup");
		envHome.mkdirs();
		logger.info(envHome.getAbsoluteFile());
		env.setup(envHome, false);
		store = env.getEntityStore();
		primaryIndex = store.getPrimaryIndex(String.class, FileEntry.class);
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
	
	public List<FileEntry> listAll(){
		List<FileEntry> out = new ArrayList<FileEntry>();
		EntityCursor<FileEntry> pi_cursor = primaryIndex.entities();
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
