package com.boscarine.finddup;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

/**
 * How well does this run standalone?
 * 
 * @author steven
 * 
 */
public class StandaloneTest {
	public static void main(String[] args) {
		Weld weld = new Weld();
		WeldContainer container = weld.initialize();

		container.instance().select(FileEntryStore.class).get();

		weld.shutdown();


	}
}
