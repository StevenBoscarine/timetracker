package com.boscarine.finddup.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * This is just a simple JAXB Object returned by web service.  
 * @author steven
 *
 */
@XmlRootElement
public class DuplicateReport {
	private final List<DuplicateSet> duplicates = new ArrayList<DuplicateSet>();

	@XmlElement
	public List<DuplicateSet> getDuplicates() {
		return duplicates;
	}

	@XmlRootElement
	public static class DuplicateSet {
		private String md5;

		private long fileSize;

		@XmlAttribute
		public String getMd5() {
			return md5;
		}

		public void setMd5(String md5) {
			this.md5 = md5;
		}

		@XmlAttribute
		public long getFileSize() {
			return fileSize;
		}

		public void setFileSize(long fileSize) {
			this.fileSize = fileSize;
		}

		@XmlElement
		public List<FileInfo> getFiles() {
			return files;
		}

		private final List<FileInfo> files = new ArrayList<FileInfo>();

	}

	@XmlRootElement
	public static class FileInfo {
		private String path;
		private Date lastModified;

		@XmlAttribute
		public String getPath() {
			return path;
		}

		public void setPath(String path) {
			this.path = path;
		}

		@XmlAttribute
		public Date getLastModified() {
			return lastModified;
		}

		public void setLastModified(Date lastModified) {
			this.lastModified = lastModified;
		}
	}
}
