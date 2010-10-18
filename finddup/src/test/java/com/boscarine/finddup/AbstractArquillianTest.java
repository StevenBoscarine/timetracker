package com.boscarine.finddup;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.Archives;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ByteArrayAsset;

public class AbstractArquillianTest extends Arquillian{

	@Deployment
	public static JavaArchive createTestArchive() {

		ByteArrayAsset beansContent = new ByteArrayAsset("<beans/>".getBytes());
		ArchivePath beansFileReference = ArchivePaths.create("beans.xml");
		return Archives.create("test.jar", JavaArchive.class)
		.addClasses(FindDups.class)
		.addClasses(FileEntryStore.class)
				.addManifestResource(beansContent, beansFileReference);

	}
}
