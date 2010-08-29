package com.boscarine.finddup.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.boscarine.finddup.FileEntry;
import com.boscarine.finddup.export.ReadOnlyStore;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/files")
@RequestScoped
public class FileHashRESTService {
	@GET
	public List<FileEntry> listAllFiles() {
		return ReadOnlyStore.getInstance().listAll();
	}
}