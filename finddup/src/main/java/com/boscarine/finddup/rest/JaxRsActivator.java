package com.boscarine.finddup.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * This empty, annotated {@link Application} class is the Java EE 6 "no XML"
 * approach to activate JAX-RS. Resources are served from the servlet path
 * specified in the @ApplicationPath annotation.
 * <p/>
 * NOTE As of JBoss AS 6.0.0.M3, JAX-RS does not activate properly.
 */
@ApplicationPath("/rest")
public class JaxRsActivator extends Application {
    /* class body intentionally left blank */
}
