package com.boscarine.finddup;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

@Test
public class TestBDB {

	private final Log logger = LogFactory.getLog(getClass());

	public void helloWorld() {
		MyDbEnv env = new MyDbEnv();
		File envHome = new File(System.getProperty("user.home"), ".finddup");
		envHome.mkdirs();
		logger.info(envHome.getAbsoluteFile());
		env.setup(envHome);
		EntityStore store = env.getEntityStore();
		PrimaryIndex<String, FileEntry> primaryIndex = store.getPrimaryIndex(String.class, FileEntry.class);
//		primaryIndex.put(new FileEntry("foo", 0, 0, "32"));
//		primaryIndex.put(new FileEntry("foo2", 0, 0, "33"));
//		primaryIndex.put(new FileEntry("foo", 0, 0, "32"));
//		primaryIndex.put(new FileEntry("foo2", 0, 0, "33"));
//		primaryIndex.put(new FileEntry("foo", 0, 0, "32"));
//		primaryIndex.put(new FileEntry("foo2", 0, 0, "33"));
//		
		

		EntityCursor<FileEntry> pi_cursor = primaryIndex.entities();
		try {
			for (FileEntry seci : pi_cursor) {
				logger.info(seci);
			}
			// Always make sure the cursor is closed when we are done with it.
		} finally {
			pi_cursor.close();
		
		}
		store.close();
		env.close();
	}
}
