package com.skl.cloud.remote.ipc.dto.audio;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="TalkFileList")
@XmlAccessorType(XmlAccessType.FIELD)
public class TalkFileListIO {

	@XmlElement(name="talkFile")
	private List<TalkFileIO> talkFileList;

	/**
	 * @return the talkFileList
	 */
	public List<TalkFileIO> getTalkFileList() {
		return talkFileList;
	}

	/**
	 * @param talkFileList the talkFileList to set
	 */
	public void setTalkFileList(List<TalkFileIO> talkFileList) {
		this.talkFileList = talkFileList;
	}
	
}
