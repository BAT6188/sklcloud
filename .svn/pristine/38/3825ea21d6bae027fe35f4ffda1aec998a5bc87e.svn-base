package com.skl.cloud.remote.stream;

import org.springframework.http.HttpMethod;

import com.skl.cloud.foundation.remote.SKLRemote;
import com.skl.cloud.foundation.remote.SKLRemoteException;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.foundation.remote.annotation.RRoot;
import com.skl.cloud.foundation.remote.stream.StreamRemote;
import com.skl.cloud.remote.stream.dto.LiveStreamServiceControlIO;
import com.skl.cloud.remote.stream.dto.StreamControlIO;

/**
  * @ClassName: IPCameraRemote
  * @Description: cloud request to IPC remote to get/update the device's setting and data.
  * @author yangbin
  * @date 2015年10月12日
  *
 */
public interface StreamSubSystemRemote extends SKLRemote {

    /**
      * requestForLiveStreamServiceSystem(请求流直播服务子系统) RTP
      * @Title: requestForLiveStreamServiceSystem
      * @param liveStreamServiceControlIO
      * @throws SKLRemoteException (参数说明)
      * @return XRemoteResult (返回值说明)
      * @author wangming
      * @date 2015年12月9日
     */
    @StreamRemote(uri = "/skl-cloud/cloud/stream/control", streamType = "LiveService", method = HttpMethod.POST)
    public XRemoteResult requestForLiveStreamServiceSystem(@RRoot LiveStreamServiceControlIO liveStreamServiceControlIO)
            throws SKLRemoteException;
    
    /**
      * requestForLiveStreamProcessSystem(请求直播流处理子系统) RTP
      * @Title: requestForLiveStreamProcessSystem
      * @param streamControlIO
      * @throws SKLRemoteException (参数说明)
      * @return XRemoteResult (返回值说明)
      * @author wangming
      * @date 2015年12月7日
     */
    @StreamRemote(uri = "/skl-cloud/cloud/stream/control", streamType = "LiveDispose", method = HttpMethod.POST)
    public XRemoteResult requestForLiveStreamProcessSystem(@RRoot StreamControlIO streamControlIO)
            throws SKLRemoteException;
    
    /**
      * requestForStreamAccessSystem(请求流接入子系统) RTP
      * @Title: requestForStreamAccessSystem
      * @param streamControlIO
      * @throws SKLRemoteException (参数说明)
      * @return XRemoteResult (返回值说明)
      * @author wangming
      * @date 2015年12月7日
     */
    @StreamRemote(uri = "/skl-cloud/cloud/stream/control", streamType = "StreamAccess", method = HttpMethod.POST)
    public XRemoteResult requestForStreamAccessSystem(@RRoot StreamControlIO streamControlIO)
            throws SKLRemoteException;
}
