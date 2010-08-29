package com.boscarine.finddup;

import java.io.File;
import java.util.List;

public class HashDisplay {
	private final String md5;
	private final long fileSize;
	private final List<File> files;

	public HashDisplay(String md5, long fileSize, List<File> files) {
		super();
		this.md5 = md5;
		this.fileSize = fileSize;
		this.files = files;
	}

	public String getMd5() {
		return md5;
	}

	public long getFileSize() {
		return fileSize;
	}

	public List<File> getFiles() {
		return files;
	}
}
