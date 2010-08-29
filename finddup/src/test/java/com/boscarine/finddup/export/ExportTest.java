package com.boscarine.finddup.export;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.testng.annotations.Test;

import com.boscarine.finddup.FileEntry;

@Test(enabled=false)
public class ExportTest {
	public void test() {
		ReadOnlyBDBEnvironment env = new ReadOnlyBDBEnvironment();
		env.setup();
		ReadOnlyStore store = ReadOnlyStore.getInstance();
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("/home/steven/dboutput.txt"));
			for (FileEntry entry : store.listAll()) {
				// TODO: Finish me
				out.write(entry.toString());
			}
			out.close();
		} catch (IOException e) {
		}
		// primaryIndex = store.getPrimaryIndex(String.class, FileEntry.class);
		// secondaryIndex = store.getSecondaryIndex(primaryIndex, String.class,
		// "md5");
	}
}
