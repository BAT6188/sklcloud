package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="musicFile")
//对属性进行排序输出,添加排序条件，必须要对类的属性添加@XmlElement注解
@XmlType(propOrder = { "id", "filename", "displayName", "fileSize", "fileurl",  "length" , "location"})
@XmlAccessorType(XmlAccessType.FIELD)
public class MusicFileIO {
	@XmlElement(name="id")
	private Long id;
	// 显示名称
	@XmlElement(name="displayName")
	private String displayName;
	// 最大长度50Bytes
	@XmlElement(name="fileName")
	private String filename;
	// 该文件的总播放时间长度，ms为单位
	@XmlElement(name="length")
	private Long length;
	// 文件大小
	@XmlElement(name="fileSize")
	private Long fileSize;
	// 文件播放的当前时间位置，ms为单位。正在播放的文件(即该文件 Play, Pause)该值有意义，
	// 如果文件未播放(stop),  那么该值为4294967295(xml定义的最大无符号integer 0xffffffff)
	@XmlElement(name="location")
	private Long location;
	// 文件url
	@XmlElement(name="fileUrl")
	private String fileurl;
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}
	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}
	/**
	 * @return the length
	 */
	public Long getLength() {
		return length;
	}
	/**
	 * @param length the length to set
	 */
	public void setLength(Long length) {
		this.length = length;
	}
	/**
	 * @return the location
	 */
	public Long getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(Long location) {
		this.location = location;
	}
	/**
	 * @return the fileurl
	 */
	public String getFileurl() {
		return fileurl;
	}
	/**
	 * @param fileurl the fileurl to set
	 */
	public void setFileurl(String fileurl) {
		this.fileurl = fileurl;
	}
	/**
	 * @return the fileSize
	 */
	public Long getFileSize() {
		return fileSize;
	}
	/**
	 * @param fileSize the fileSize to set
	 */
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	
	
}
