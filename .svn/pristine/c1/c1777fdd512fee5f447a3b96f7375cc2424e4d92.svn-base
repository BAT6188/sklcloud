package com.skl.cloud.dao.audio;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.audio.Story;

public interface StoryMapper {
    
	/**
	 * 根据sn查询story列表
	 * @param sn
	 * @return
	 */
	List<Story> getStoryListBySN(@Param("sn") String sn, @Param("status") int status);
    
	/**
	 * 根据storyId获取某个设备story列表的特定一项
	 * @param sn
	 * @param storyId
	 * @return
	 */
	Story getStoryByIdSn(@Param("storyId") Long storyId, @Param("sn") String sn, @Param("status") int status);
    
	/**
	 * 新建某项story设置
	 * @param story
	 */
	void setStoryList(Story story);
    
	/**
     * 更改story列表的某一项设置
     * @param story
     */
	void modifyStoryList(Story story);
   
	/**
     * 根据story列表的ID虚拟删除story，不是物理删除，只是改变status值
     * @param storyId
     * @param statusDel
     */
	void setStoryStatus(@Param("storyId") Long storyId, @Param("status") int status);
    
	/**
	 * 修改story的taskId
	 * @param storyId
	 */
	void setTaskId(Story story);

	/**
	 * 查询story表是否已存在该taskId
	 * @Description: 已存在该taskId,返回true
	 * @param taskId
	 * @return Boolean
	 */
	Boolean isExistTaskId(String taskId);   
	
	/**
	 * 根据storyId获取story列表的特定一项
	 * @param storyId
	 * @return
	 */
	Story getStoryById(Long storyId);
}