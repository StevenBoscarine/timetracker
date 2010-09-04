package com.boscarine.finddup;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(enabled = false)
public class FindTest {
    @SuppressWarnings("unused")
    private final Log logger = LogFactory.getLog(getClass());

    public void simpleExceptionTest() {
        FindDups findDups = new FindDups();
        Map<String, List<FileEntry>> txt = findDups.findDuplicates("*.txt");
        Map<String, List<FileEntry>> java = findDups.findDuplicates("*.java");
        logger.info(txt.size() + " text duplicates " + java.size() + " java duplicates");
        Assert.assertTrue(txt.size() > 0);
    }
}
