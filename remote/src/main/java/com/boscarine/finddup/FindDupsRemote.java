package com.boscarine.finddup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: steven Date: Sep 4, 2010 Time: 12:12:04 PM To change this template use File |
 * Settings | File Templates.
 */
@Named
@SessionScoped
public class FindDupsRemote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected final Log logger = LogFactory.getLog(getClass());
	private String url;
	private String filePattern;
	private DuplicateReport results;
	private String lastPattern;
	private FindDupsClient client;
	private Map<String, DuplicateReport> cache=new HashMap<String, DuplicateReport>();
	public List<String> getSelectedFiles() {
		return selectedFiles;
	}

	public void setSelectedFiles(List<String> selectedFiles) {
		logger.info("I was called (setSelectedFiles)");
		System.out.println("I was called (setSelectedFiles)");
		this.selectedFiles = selectedFiles;
	}

	private List<String> selectedFiles = new ArrayList<String>();

	public void findDups() {
		if (url == null) {
			throw new RuntimeException("URL is null!");
		}
		if (filePattern.equals(lastPattern)) {
			return;
		}
		if (client == null) {
			System.out.println("constructing a new client.");
			client = new FindDupsClient(url);
		}else{
			System.out.println("using previously instantiated client");
		}
		if(!cache.containsKey(filePattern)){
			cache.put(filePattern, client.get("rest/finddups/2/" + filePattern, DuplicateReport.class));
		}else{
			System.out.println("using cache");
		}
		results = cache.get(filePattern);
		lastPattern = filePattern;
	}

	public String handleDups() {
		if (selectedFiles.size() == 0) {
			logger.error("Nothing selected!!");

		} else {
			int i = 0;

			for (String file : selectedFiles) {
				i++;
				System.out.print(i);
				System.out.println(file);
			}
		}
		return "results";
	}

	public String getFilePattern() {
		return filePattern;
	}

	public void setFilePattern(String filePattern) {
		this.filePattern = filePattern;
	}

	public DuplicateReport getResults() {
		return results;
	}

	public void setResults(DuplicateReport results) {
		this.results = results;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
