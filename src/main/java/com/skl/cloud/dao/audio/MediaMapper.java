package com.skl.cloud.dao.audio;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.audio.Media;

public interface MediaMapper {
	
	/**
	 * 根据ID获得Media
	 * @param id
	 * @return
	 */
	Media getMedia(Long id);
	
	/**
	 * 根据SN获得云端媒体文件
	 * @param sn
	 * @param type
	 * @return
	 */
	List<Media> listMediaBySN(@Param("sn") String sn, @Param("mediaType") int mediaType);
	
	/**
	 * 根据fileName获得Media
	 * @param fileName
	 * @return
	 */
	Media getMediaByName(@Param("fileName") String fileName);

	/**
	 * 根据media的类型（mediaType）查询media表系统预设（sourceType=0）的media
	 * @param mediaType
	 * @return
	 */
	List<Media> listSysMediaByType(int mediaType);
	
}