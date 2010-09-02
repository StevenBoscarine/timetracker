// file MyDbEnv.java

package com.boscarine.finddup.export;

import com.boscarine.finddup.ReadWriteBDBEnvironment;

/**
 * Berkeley DB specific boilerplate
 * 
 * @author steven
 * 
 */

public class ReadOnlyBDBEnvironment extends ReadWriteBDBEnvironment {
	@Override
	public void setup() {
		setup(getDefaultHomeDir(), true);
	}
}