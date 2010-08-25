package com.boscarine.finddup;

import java.util.Date;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

@Entity
public class FileEntry {
	private FileEntry() {
		super();
	}

	@PrimaryKey
	private String fileName;
	private long lastModified;
	private long size;
	@SecondaryKey(relate = Relationship.MANY_TO_ONE)
	private String md5;

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
