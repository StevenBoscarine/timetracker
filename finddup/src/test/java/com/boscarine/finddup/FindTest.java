package com.boscarine.finddup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

@Test
public class FindTest {
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(getClass());
	Find find = new Find();
	//
	// public void initialTest() throws IOException {
	//
	// FileEntryStore store = new FileEntryStore();
	// Collection<FileEntry> entries= find.createFileEntries("*.avi", store);
	// for(FileEntry entry : entries){
	// // System.out.println(entry);
	// }
	// store.shutdown();
	// }
}
