package com.boscarine.finddup;

import java.io.File;

import org.testng.annotations.Test;

import com.sleepycat.persist.EntityCursor;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
@Test
public class QueryBDB {
	public void test(){
		FileEntryStore store = new FileEntryStore();
		for(FileEntry entry : store.listAll()){
			System.out.println(entry);
		}
	}
}
