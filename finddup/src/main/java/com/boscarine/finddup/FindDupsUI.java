package com.boscarine.finddup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * Backing bean
 */
@Named
@RequestScoped
public class FindDupsUI {
	private FindDup findDup;

	public void setFilePattern(String filePattern) {
		System.err.println("the setter was called");
		this.filePattern = filePattern;
		dups = transformDuplicatesToUIObjects(findDup.findDuplicates(filePattern));
	}

    private List<String> toDiscard;

    public List<String> getToDiscard() {
        return toDiscard;
    }

    public void delete(){
        System.out.println("Delete called");
    }

    public void setToDiscard(List<String> toDiscard) {
        this.toDiscard = toDiscard;
    }

    private String filePattern = "*.mov";

	// tmp

	public String getFilePattern() {
		return filePattern;
	}

	private static List<HashDisplay> dups;

	public List<HashDisplay> transformDuplicatesToUIObjects(Map<String, List<FileEntry>> input) {
		System.err.println("I was called");
		List<HashDisplay> out = new ArrayList<HashDisplay>();
		for (String key : input.keySet()) {
			List<FileEntry> entries = input.get(key);
			List<File> files = new ArrayList<File>(entries.size());
			for (FileEntry entry : entries) {
				files.add(new File(entry.getFileName()));
			}
			out.add(new HashDisplay(key, files.get(0).length(), files));
		}
		return out;
	}

	public synchronized List<HashDisplay> getDups() {

		return dups;
	}

	public FindDupsUI() {
		super();
	}

	@Inject
	public FindDupsUI(FindDup findDup) {
		this();
		this.findDup = findDup;
	}
}