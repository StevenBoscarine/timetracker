package com.boscarine.finddup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.annotations.Test;

import com.boscarine.finddup.FindDupsClient;

@Test(enabled=false)
public class TestFindDups {
	protected final Log logger = LogFactory.getLog(getClass());
	public void test(){
		logger.debug("hello world");
		logger.info("hello world");
		FindDupsClient client = new FindDupsClient("http://localhost:8090/finddup/");
		client.get("rest/finddups/2/*.mov", DuplicateReport.class);
	}
}
