package com.boscarine.finddup.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.boscarine.finddup.FileEntry;
import com.boscarine.finddup.FileEntryStore;
import com.boscarine.finddup.FindDups;
import com.boscarine.finddup.FindDupsUI;
import com.boscarine.finddup.HashDisplay;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@RequestScoped
@Path("/finddups")
public class FileHashRESTService
{
   final FileEntryStore instance = FileEntryStore.getInstance();

   private final Log logger = LogFactory.getLog(getClass());
   @Inject
   private FindDupsUI ui;
   
   
   @GET
   @Path("/listAll")
   public List<FileEntry> listAllFiles()
   {
      return instance.listAll();
   }

//   @GET
//   @Path("/finddups/{extension}")
//   public Map<String, List<FileEntry>> finddups(@PathParam("extension") String extension)
//   {
//      FindDups tmp = new FindDups();
//      return tmp.findDuplicates(extension);
//   }

   @GET
   @Path("/{filePattern}")
   public List<HashDisplay> finddups(@PathParam("filePattern") String filePattern)
   {
      FindDups tmp = new FindDups();
      List<HashDisplay> dups = ui.findDuplicates(filePattern);
      logger.info("found " + dups.size() + " duplicate files");
      return dups;
//      return tmp.findDuplicates(extension);
   }
   
   
}