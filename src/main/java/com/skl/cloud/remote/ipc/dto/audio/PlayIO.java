package com.skl.cloud.remote.ipc.dto.audio;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="play")
//对属性进行排序输出
@XmlType(propOrder = { "filetype", "filename", "displayName", "fileUrl", "fileSize", "length" , "location"})
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayIO {
    //文件格式
	@XmlElement(name="filetype")
	private String filetype;
	//播放列表中的文件的名字, 最大长度50Bytes
	@XmlElement(name="filename")
	private String filename;
	//该文件的总播放时间长度，ms为单位
	@XmlElement(name="length")
	private Long length;
	//文件播放的当前时间位置，ms为单位
	@XmlElement(name="location")
	private Long location;
	//文件存储的路径
	@XmlElement(name="fileUrl")
	private String fileUrl;
	//文件的大小
	@XmlElement(name="fileSize")
	private Long fileSize; 
	//该条schedule显示的名称
	@XmlElement(name="displayname")
	private String displayName;
	
	
	/**
	 * @return the filetype
	 */
	public String getFiletype() {
		return filetype;
	}
	/**
	 * @param filetype the filetype to set
	 */
	public void setFiletype(String filetype) {
		this.filetype = filetype;
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
	 * @return the fileUrl
	 */
	public String getFileUrl() {
		return fileUrl;
	}
	/**
	 * @param fileUrl the fileUrl to set
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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
