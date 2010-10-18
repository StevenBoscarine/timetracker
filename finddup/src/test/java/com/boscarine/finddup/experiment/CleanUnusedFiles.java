package com.boscarine.finddup.experiment;

import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.boscarine.finddup.AbstractArquillianTest;
import com.boscarine.finddup.FileEntryStore;

@Test(enabled=false)
public class CleanUnusedFiles extends AbstractArquillianTest {
	public void clean() {
		Assert.assertNotNull(store);
		store.deleteOrphanedFiles();
	}

	@Inject
	private FileEntryStore store;
}
