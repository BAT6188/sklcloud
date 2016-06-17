package com.skl.cloud.controller.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.amazonaws.HttpMethod;
import com.skl.cloud.common.exception.BusinessException;
import com.skl.cloud.controller.common.BaseController;
import com.skl.cloud.foundation.file.S3Factory;
import com.skl.cloud.foundation.remote.XRemoteResult;
import com.skl.cloud.model.S3FileData;
import com.skl.cloud.model.audio.AudioPlay;
import com.skl.cloud.service.S3Service;
import com.skl.cloud.service.audio.AudioPlayListService;
import com.skl.cloud.service.user.AppUserService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;

/**
 * @ClassName: AudioMusicPlayListController
 * @Description: TODO
 * @author lizhiwei
 * @date 2016年1月14日 上午10:08:04
 */

@Controller
@RequestMapping("/skl-cloud/app")
public class AudioPlayListController extends BaseController
{
	//enable upload max file size is 10M
	public static final long UPLOAD_MAX_FILE_SIZE = 10 * 1024 * 1024;
	
	@Autowired(required = true)
	private AudioPlayListService audioPlayListService;

	@Autowired(required = true)
	private AppUserService appUserService;

	@Autowired(required = true)
	private S3Service s3Service;

	/**
	 * @Title: getCustomMusicList
	 * @Description: APP获取云端存放的指定IPC设备的音乐文件列表
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2016年1月19日 上午10:10:56
	 */
	@RequestMapping("/Custom/MusicList.app")
	public ResponseEntity<String> getCustomMusicList(HttpServletRequest req)
	{
		String sReturn = null;
		try
		{
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(req);

			String userID = XmlUtil.convertToString(paraMap.get("userId"), true);
			String sn = XmlUtil.convertToString(paraMap.get("SN"), false);

			List<Map<String, Object>> list = audioPlayListService.getAudioPlayList(sn);

			sReturn = responseXml("0", "0", list);
		} catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}

	private String responseXml(String status, String statusString, List<Map<String, Object>> list)
	{
		StringBuffer sb = new StringBuffer("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<musicPlayFileList  version=\"1.0\" xmlns=\"urn:skylight\">");
		sb.append("<musicFileList>");
		if (list != null)
		{
			for (Map<String, Object> map : list)
			{
				sb.append("<musicFile>");
				sb.append("<id>" + map.get("pictureId") + "</id>");
				sb.append("<fileName>" + map.get("fileName") + "</fileName>");
				sb.append("<uuid>" + map.get("uuid") + "</uuid>");
				sb.append("<sequence>" + map.get("sequence") + "</sequence>");
				sb.append("<fileLen>" + map.get("fileLen") + "</fileLen>");
				sb.append("<url>" + S3Factory.getDefault().getPresignedUrl(StringUtil.convertToS3Key(String.valueOf(map.get("fileUrl")) + String.valueOf(map.get("fileName"))), HttpMethod.GET) + "</url>");
				sb.append("</musicFile>");
			}
		}
		sb.append("</musicFileList>");
		sb.append("<ResponseStatus>");
		sb.append("<statusCode>" + status + "</statusCode>");
		sb.append("<statusString>" + statusString + "</statusString>");
		sb.append("</ResponseStatus>");
		sb.append("</musicPlayFileList>");
		return sb.toString();
	}

	/**
	 * @Title: issuedAudioList
	 * @Description: APP向指定IPC设备下发play音乐列表
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2016年1月19日 上午16:10:56
	 */
	@RequestMapping("/{SN}/Custom/Music/playList.app")
	public ResponseEntity<String> issuedAudioList(HttpServletRequest req, @PathVariable String SN)
	{
		String sReturn = null;
		try
		{
			String reqXml = XmlUtil.isChangeToStr(req.getInputStream());
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(reqXml);

			List<AudioPlay> list = new ArrayList<AudioPlay>();
			for (Object musicFileObj : (List) paraMap.get("musicFileList"))
			{
				Map<String, Object> musicFileMap = (Map<String, Object>) ((Map<String, Object>) musicFileObj).get("musicFile");
				String id = XmlUtil.convertToString(musicFileMap.get("id"), false);
				String fileName = XmlUtil.convertToString(musicFileMap.get("fileName"), false);
				String uuid = XmlUtil.convertToString(musicFileMap.get("uuid"), false);
				int sequence = XmlUtil.convertToInt(musicFileMap.get("sequence"), false);
				String url = XmlUtil.convertToString(musicFileMap.get("url"), false);

				if(sequence < 0 || sequence > 10)
				{
					throw new BusinessException("0x50020082");
				}
				
				S3FileData fileData = s3Service.getCheckUploadFile(uuid, null, SN);
				if (!fileName.equals(fileData.getFileName()) || !url.contains(fileData.getFileName()))
				{
					throw new BusinessException("0x50020079");
				}

				AudioPlay audio = new AudioPlay();
				audio.setPictureId(id);
				audio.setUuid(fileData.getUuid());
				audio.setSequence(sequence);
				audio.setSn(SN);
				audio.setActiveFlag("0");
				audio.setCreateTime(new Date());
				audio.setDisplayName(fileName);
				audio.setStatus("2");
				list.add(audio);

			}
			
			// 下发更新audio列表
			audioPlayListService.updateAudioPlay(list);

			XRemoteResult audioPlayResult = audioPlayListService.issueAudioPlay(SN, reqXml);

			if (audioPlayResult == null || !"0".equals(audioPlayResult.getStatusCode()))
			{
				throw new BusinessException("0x50010032");// 通知IPC下发play音乐列表时，返回的结果异常
			}

			sReturn = XmlUtil.mapToXmlRight();
		} catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}

		return new ResponseEntity<String>(sReturn, HttpStatus.OK);

	}

	/**
	 * @Title: issuedAudioList
	 * @Description: APP对指定IPC设备上音乐文件的操作
	 * @param req
	 * @return ResponseEntity<String>
	 * @author lizhiwei
	 * @date 2016年1月19日 上午16:10:56
	 */
	@RequestMapping("/{SN}/Custom/Music/play ")
	public ResponseEntity<String> commandAudioPlay(HttpServletRequest req, @PathVariable String SN)
	{
		String sReturn = null;

		try
		{
			String reqXml = XmlUtil.isChangeToStr(req.getInputStream());
			Map<String, Object> paraMap = XmlUtil.getRequestXmlParam(reqXml);

			String fileName = XmlUtil.convertToString(paraMap.get("fileName"), false);
			String command = XmlUtil.convertToString(paraMap.get("command"), false);
			String delay = XmlUtil.convertToString(paraMap.get("delay"), false);

			Map<String, Object> audioMap = audioPlayListService.getAudioPlayBySN(SN, fileName);

			if (audioMap == null || !"1".equals(String.valueOf(audioMap.get("status"))))
			{
				throw new BusinessException("0x50010034");
			}

			XRemoteResult audioPlayResult = audioPlayListService.commandAudioPlay(SN, reqXml);

			if (audioPlayResult == null || !"0".equals(audioPlayResult.getStatusCode()))
			{
				throw new BusinessException("0x50010033");// 对指定IPC设备上音乐文件的操作时，返回的结果异常
			}
			sReturn = XmlUtil.mapToXmlRight();

		} catch (Exception e)
		{
			sReturn = getErrorXml(e, this.getClass().getName());
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

}
