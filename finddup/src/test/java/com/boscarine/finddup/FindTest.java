package com.boscarine.finddup;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

@Test(enabled = false)
public class FindTest extends AbstractArquillianTest{
	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(getClass());
	@Inject
	private FindDups findDups;// = new FindDups();
	/**
	 * Can we do a run of the code without throwing a runtime exception?
	 */
	public void simpleExceptionTest() {
		// FindDups findDups = new FindDups();
		// Map<String, List<FileEntry>> txt = findDups.findDuplicates("*.txt");
		// Map<String, List<FileEntry>> java = findDups.findDuplicates("*ATK*.jpg");
		// Map<String, List<FileEntry>> tmp = findDups.findDuplicates("*ari*.jpg");
		// Map<String, List<FileEntry>> tmp2 = findDups.findDuplicates("*aph*.jpg");
		// findDups.findDuplicates("*.zip");
		findDups.findDuplicates("*duplicate*.txt");
		// logger.info(txt.size() + " text duplicates " + java.size() + " java duplicates");
		// Assert.assertTrue(txt.size() > 0);
	}

	public void seedDatabase() {

		findDups.findDuplicates("*.mp3");

	}
	@AfterSuite
	public void shutdown(){
		findDups.shutdown();
	}
}
