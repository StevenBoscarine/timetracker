package com.boscarine.finddup;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.txw2.annotation.XmlElement;
/**
 * Bean returned by service layer to GUI
 * @author steven
 *
 */
@XmlRootElement
public class HashDisplay {
    private String md5;
    private long fileSize;
    private List<File> files;

    private HashDisplay() {
        super();
    }

    public HashDisplay(String md5, long fileSize, List<File> files) {
        this();
        this.md5 = md5;
        this.fileSize = fileSize;
        this.files = files;
    }

    @XmlAttribute
    public String getMd5() {
        return md5;
    }

    @XmlAttribute
    public long getFileSize() {
        return fileSize;
    }

    @XmlElement
    public List<File> getFiles() {
        return files;
    }
}
