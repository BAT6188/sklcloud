package com.skl.cloud.controller.ipc;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.model.PlatformLog;
import com.skl.cloud.model.ipc.IPCamera;
import com.skl.cloud.service.ipc.IPCameraService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.util.common.DateUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * 流状态及其数据统计分析
 * @author fulin
 *
 */
@RequestMapping("/skl-cloud/device")
@Controller
public class IpcPushStreamStatusController extends IpcController
{
	private static Logger logger = Logger.getLogger(IpcPushStreamStatusController.class);
	
	@Autowired(required = true)
	private IPCameraService iPCameraService;
	
	@Autowired(required = true)
	private StreamStatusService streamStatusService;


	/**
	 * IPC主动上报推流状态到云端,同时业控对Relay或P2P成功的次数做统计
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "/upload/pushStream/status", method = RequestMethod.POST)
	public ResponseEntity<String> ipcReportPushStreamStatus(HttpServletRequest req)
	{
		String sReturn = null;

		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);
			
			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);
			String pushStreamType = XmlUtil.convertToString(paraMap.get("pushStreamType"), false);
			String streamStatus = XmlUtil.convertToString(paraMap.get("streamStatus"), false);

			// 查询ipc是否存在
			IPCamera ipCamera = iPCameraService.getIPCameraBySN(sn);
			if (ipCamera == null)
			{
				throw new BusinessException("0x50000047");
			}
			logger.info("--IPC upload pushStream，SN: "+sn+", pushStreamType: "+pushStreamType+", streamStatus: "+streamStatus);
			//记录ipc推流状态，并统计成功次数和播放总时间等
			streamStatusService.countStreamStatus(sn, pushStreamType, streamStatus);
			
			sReturn = XmlUtil.mapToXmlRight();
			
			PlatformLog pl = new PlatformLog();
			pl.setUserId(10002l);
			pl.setSn(sn);
			pl.setModuleName("IPC主动上报推流状态到云端");
			pl.setLogContent("上报SN：" + sn + " 出流类型为: "+pushStreamType+", 流动作: "+streamStatus);
			pl.setLogTime(DateUtil.date2string(new Date(), DateUtil.DATE_TIME_1_FULL_FORMAT));
			super.saveLog(pl);
		}
		catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
	
}
