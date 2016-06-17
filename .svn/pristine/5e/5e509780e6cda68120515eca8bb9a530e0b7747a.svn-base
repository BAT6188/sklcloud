package com.skl.cloud.remote.ipc;

import org.springframework.http.HttpMethod;

import com.skl.cloud.foundation.remote.SKLRemote;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.foundation.remote.annotation.RRoot;
import com.skl.cloud.foundation.remote.annotation.RText;
import com.skl.cloud.foundation.remote.annotation.RVariable;
import com.skl.cloud.foundation.remote.ipc.IPCRemote;
import com.skl.cloud.foundation.remote.ipc.SN;
import com.skl.cloud.remote.ipc.dto.ipc.AudioChannelIO;
import com.skl.cloud.remote.ipc.dto.ipc.IPCStreamControlIO;
import com.skl.cloud.remote.ipc.dto.ipc.RequestCurrentPictrueIO;

/**
  * @ClassName: IPCameraRemote
  * @Description: cloud request to IPC remote to get/update the device's setting and data.
  * @author yangbin
  * @date 2015年10月12日
  *
 */
public interface IPCameraRemote extends SKLRemote {

    /**
     * 更新IPC Remote的音量信息
     * @param sn the camera serial number
     * @param id the camera id
     * @return
     * @throws SKLRemoteException
     */
    @IPCRemote(uri = "/PSIA/System/Audio/channels/{id}", rootName = "audioChannel", xmln="urn:skylight", method = HttpMethod.PUT)
    public void updateSpeakerVolume(@SN String sn, @RVariable("id") String id, @RRoot AudioChannelIO audioChannelIO)
            throws SKLRemoteException;

    /**
     * 获取IPC Remote的音量信息
     * @param sn the camera serial number
     * @param id the camera id
     * @return
     * @throws SKLRemoteException
     */
    @IPCRemote(uri = "/PSIA/System/Audio/channels/{id}", method = HttpMethod.GET)
    public AudioChannelIO getSpeakerVolume(@SN String sn, @RVariable("id") String id) throws SKLRemoteException;

	
    /**
     * 获取IPC的设备信息
     * @param sn the camera serial number
     * @return
     * @throws SKLRemoteException
     */
    @IPCRemote(uri="/device/{SN}/system/deviceInfo", method=HttpMethod.GET)
    public   String  getDeciceFWInfo(@RVariable("SN") @SN String sn)throws SKLRemoteException;
    
    /**
      * informIPCUploadStream(通知IPC上传视频流)
      * @Title: informIPCUploadStream
      * @param sn
      * @throws SKLRemoteException (参数说明)
      * @author wangming
      * @date 2015年12月7日
     */
    @IPCRemote(uri="/PSIA/Streaming/streamingControl",rootName="streamControl", xmln="urn:skylight", method=HttpMethod.POST)
    public XRemoteResult ipcStreamControl(@RVariable("SN") @SN String sn,@RRoot IPCStreamControlIO iPCStreamControlIO)throws SKLRemoteException;

    /**
      * getStreamChannelInfo(获取指定IPC的流通道参数信息)
      * @Title: getStreamChannelInfo
      * @param sn
      * @param id
      * @throws SKLRemoteException (参数说明)
      * @author wangming
      * @date 2015年12月8日
     */
    @IPCRemote(uri = "/PSIA/Streaming/channels/simple/{id}", method = HttpMethod.GET)
    public String getStreamChannelInfo(@SN String sn, @RVariable("id") String id) throws SKLRemoteException;
   
    /**
     * 
      * setStreamEnable(设置IPC流使能开关为true)
      * @Title: setStreamEnable
      * @param sn
      * @param id
      * @throws SKLRemoteException (参数说明)
      * @author wangming
      * @date 2015年12月8日
     */       
    @IPCRemote(uri = "/PSIA/Streaming/channels/simple/{id}",  rootName="StreamingChannel", xmln="urn:skylight", method = HttpMethod.PUT)
    public String setStreamEnable(@SN String sn, @RVariable("id") String id,@RText String ipcInfo) throws SKLRemoteException;
    
    /**
     * 
      * issueAudioPlay(向指定IPC设备下发play音乐列表)
      * @Title: issueAudioPlay
      * @param sn
      * @param xml
      * @throws SKLRemoteException (参数说明)
      * @author lizhiwei
      * @date 2016年1月20日
     */
    @IPCRemote(uri="/Custom/Music/playList",rootName="musicPlayFileList", xmln="urn:skylight", method=HttpMethod.POST)
	public XRemoteResult  issueAudioPlay(@RVariable("SN") @SN String sn, @RText String xml) throws SKLRemoteException;
    
	/**
     * 
     * commandAudioPlay(对指定IPC设备上音乐文件的操作)
     * @Title: commandAudioPlay
     * @param sn
     * @param xml
     * @throws SKLRemoteException (参数说明)
     * @author lizhiwei
     * @date 2016年1月20日
     */
    @IPCRemote(uri="/Custom/Music/play",rootName="musicPlayCommand", xmln="urn:skylight", method=HttpMethod.POST)
    public XRemoteResult  commandAudioPlay(@RVariable("SN") @SN String sn, @RText String xml) throws SKLRemoteException;
   
    /**
     * Cloud 向IPC请求IPC实时拍照
     * @param sn
     * @param requestCurrentPictrueIO
     * @return
     * @throws SKLRemoteException
     */
    @IPCRemote(uri = "/PSIA/Streaming/channels/{SN}/picture/upload", rootName="pictureUrl",  xmln="urn:skylight", method = HttpMethod.POST)
    public void requestCurrentPictrue(@RVariable("SN") @SN String sn, @RRoot RequestCurrentPictrueIO requestCurrentPictrueIO) throws SKLRemoteException;

    /**
     * cloud 请求ipc修改密码
     * @param id
     * @param sn
     * @param xml
     * @throws SKLRemoteException
     */
    @IPCRemote(uri = "/PSIA/Security/AAA/users/{id}", method = HttpMethod.POST)
    public void modifyDevicePassword(@RVariable("id") int id,@SN String sn, @RText String xml) throws SKLRemoteException;
}
