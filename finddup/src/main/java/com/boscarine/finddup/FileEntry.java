package com.boscarine.finddup;

import java.util.Date;

public class FileEntry {

	private final String fileName;
	private final long lastModified;
	private final long size;
	private final String md5;

	public FileEntry(String fileName, long lastModified, long size, String md5) {
		super();
		this.fileName = fileName;
		this.lastModified = lastModified;
		this.size = size;
		this.md5 = md5;
	}

	public String getFileName() {
		return fileName;
	}

	public long getLastModified() {
		return lastModified;
	}

	public long getSize() {
		return size;
	}

	public String getMd5() {
		return md5;
	}

	@Override
	public String toString() {
		return fileName + "[lastModified=" + new Date(lastModified) + ", size=" + size + ", md5=" + md5 + "]";
	}

}
