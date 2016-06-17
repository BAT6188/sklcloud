package com.skl.cloud.dao.audio;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.skl.cloud.model.audio.Alarm;



public interface AlarmMapper {
    /**
     * 根据设备的SN查询alarm列表
     * @param sn
     * @return
     */
	 List<Alarm> getAlarmListBySN(@Param("sn") String sn, @Param("status") int status);

	 /**
	  * 根据alarmId查询某个设备的alarm列表中的某一项
	  * @param alarmId
	  * @return
	  */
	Alarm getAlarmByIdSn(@Param("alarmId") Long alarmId, @Param("sn") String sn, @Param("status") int status); 
    
	/**
	 * 新增一条alarm
	 * @param alarm
	 */
	void addAlarm(Alarm alarm);
    
	/**
	 * 更改某条alarm
	 * @param alarm
	 */
	void modifyAlarm(Alarm alarm);
    
	/**
	 * 根据alarm列表的ID虚拟删除alarm，不是物理删除，只是改变status值
	 * @param alarmId
	 * @param statusDel
	 */
	void setAlarmStatus(@Param("alarmId") Long alarmId, @Param("status") int status);
    
	/**
	 * 修改alarm的taskId
	 * @param alarm
	 */
	void setTaskId(Alarm alarm);
    
	/**
	 * 查询alarm表是否已存在该taskId
	 * @Description: 已存在该taskId,返回true
	 * @param taskId
	 * @return Boolean
	 */
	Boolean isExistTaskId(String taskId);

	/**
	 * 根据alarmId查询alarm列表中的某一项
	 * @param alarmId
	 * @return
	 */
	Alarm getAlarmById(Long alarmId);
	
}