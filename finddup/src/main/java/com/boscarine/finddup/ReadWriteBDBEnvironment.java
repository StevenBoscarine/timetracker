// file MyDbEnv.java

package com.boscarine.finddup;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Berkeley DB specific boilerplate
 *
 * @author steven
 */
public class ReadWriteBDBEnvironment {
    private final Log logger = LogFactory.getLog(getClass());

    public File getDefaultHomeDir() {
        File envHome = new File(System.getProperty("user.home"), ".finddup");
        envHome.mkdirs();
        logger.info("Using Berkeley Database in " + envHome.getAbsoluteFile());
        return envHome;

    }

    protected Environment myEnv;
    protected EntityStore store;
    protected boolean readOnly = false;

    public void setup() {
        setup(getDefaultHomeDir(), false);
    }

    /**
     * The setup() method opens the environment and store for us.
     *
     * @param envHome
     * @throws DatabaseException
     */
    protected void setup(File envHome, boolean readOnly) throws DatabaseException {


        EnvironmentConfig myEnvConfig = new EnvironmentConfig();
        StoreConfig storeConfig = new StoreConfig();

        myEnvConfig.setReadOnly(readOnly);
        //TODO:  Describe why we do this.
        myEnvConfig.setTransactional(true);
        storeConfig.setReadOnly(readOnly);
        //TODO:  Describe why we do this.
        storeConfig.setTransactional(true);
        // If the environment is opened for write, then we want to be able to
        // create the environment and entity store if they do not exist.
        myEnvConfig.setAllowCreate(!readOnly);
        storeConfig.setAllowCreate(!readOnly);

        // Open the environment and entity store
        myEnv = new Environment(envHome, myEnvConfig);
        store = new EntityStore(myEnv, "EntityStore", storeConfig);

    }

    public EntityStore getEntityStore() {
        return store;
    }

    public Environment getEnv() {
        return myEnv;
    }

    /**
     * Close the store and environment
     */
    public void close() {
        if (store != null) {
            try {
                store.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing store: " + dbe.toString());
                System.exit(-1);
            }
        }

        if (myEnv != null) {
            try {
                // Finally, close the store and environment.
                myEnv.close();
            } catch (DatabaseException dbe) {
                System.err.println("Error closing MyDbEnv: " + dbe.toString());
                System.exit(-1);
			}
		}
	}
}