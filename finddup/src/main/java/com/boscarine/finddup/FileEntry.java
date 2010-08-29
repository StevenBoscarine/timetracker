package com.boscarine.finddup;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class FileEntry implements Serializable{
	private static final long serialVersionUID = 1L;

	private FileEntry() {
		super();
		this.hashDate=System.currentTimeMillis();
	}

	@PrimaryKey
	private String fileName;
	private long lastModified;
	private long size;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private String md5;
	private final long hashDate;

	public long getHashDate() {
		return hashDate;
	}

	public FileEntry(String fileName, long lastModified, long size, String md5) {
		this();
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
