package com.boscarine.finddup;

import org.testng.annotations.Test;
@Test(enabled=false)
public class QueryBDB {
	public void test(){
		FileEntryStore store = new FileEntryStore();
		for(FileEntry entry : store.listAll()){
			System.out.println(entry);
		}
	}
}
