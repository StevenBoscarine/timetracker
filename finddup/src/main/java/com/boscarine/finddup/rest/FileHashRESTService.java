package com.boscarine.finddup.rest;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.boscarine.finddup.FileEntry;
import com.boscarine.finddup.FileEntryStore;
import com.boscarine.finddup.FindDupsUI;
import com.boscarine.finddup.HashDisplay;
import com.boscarine.finddup.rest.DuplicateReport.DuplicateSet;
import com.boscarine.finddup.rest.DuplicateReport.FileInfo;

@RequestScoped
@ApplicationPath("/api")
@Path("/findDuplicates")
public class FileHashRESTService extends Application{

	@Inject
	FileEntryStore instance;
	private final Log logger = LogFactory.getLog(getClass());
	@Inject
	private FindDupsUI ui;

	@GET
	@Path("/listAll")
	public List<FileEntry> listAllFiles() {
		return instance.listAll();
	}

	@GET
	@Path("/{filePattern}")
	public DuplicateReport finddups2(@PathParam("filePattern") String filePattern) {
		List<HashDisplay> dups = ui.findDuplicates(filePattern);
		logger.info("found " + dups.size() + " duplicate files");
		DuplicateReport report = new DuplicateReport();
		for (HashDisplay tmp : dups) {
			DuplicateSet set = new DuplicateSet();
			set.setFileSize(tmp.getFileSize());
			set.setMd5(tmp.getMd5());
			for (File file : tmp.getFiles()) {
				FileInfo fileInfo = new FileInfo();
				fileInfo.setLastModified(new Date(file.lastModified()));
				fileInfo.setPath(file.getAbsolutePath());
				set.getFiles().add(fileInfo);
			}
			report.getDuplicates().add(set);
		}
		return report;
	}
}