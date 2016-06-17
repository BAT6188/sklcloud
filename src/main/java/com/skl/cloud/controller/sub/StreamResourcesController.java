package com.skl.cloud.controller.sub;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.skl.cloud.foundation.mvc.method.annotation.ResponseName;
import com.skl.cloud.foundation.mvc.model.SKLModel;
import com.skl.cloud.service.StreamResourcesService;
import com.skl.cloud.service.ipc.StreamStatusService;
import com.skl.cloud.util.constants.Constants;

/**
 * 
  * @ClassName: StreamResourcesController
  * @Description: IPC连接指令中心成功后，指令中心主动触发对应流类型（RTP、RTSP）的流资源调度控制过程。
  * @author wangming
  * @date 2015年12月24日
  *
 */
@Controller
@RequestMapping("skl-cloud")
public class StreamResourcesController extends SubController {

	private static final Logger logger = Logger.getLogger(StreamResourcesController.class);

	@Autowired
	private StreamResourcesService streamResourcesService;

	@Autowired
	private StreamStatusService streamStatusService;

	@RequestMapping("/cloud/ipcCommand/connect")
	public ResponseEntity<String> replySuccess(InputStream inputStream, HttpServletRequest request,
			HttpServletResponse response) { 
		// 返回给APP的报文数据
		String sReturn = "";
		
		StringBuffer sb = new StringBuffer("<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand>");
		sb.append("<status>");
		sb.append("0");
		sb.append("</status>");
		sb.append("</ipcCommand>");

		Map<String, String> xmlMap = new HashMap<String, String>();

		try {
			xmlMap = this.parseIpcInstructionCenterRequest(inputStream);
		} catch (Exception e) {
			logger.error("It is incorrect when inputstream is changed into xml file", e);
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";
			return new ResponseEntity<String>(sReturn, HttpStatus.OK);
		}

		// 获取产品型号
		String productModel = xmlMap.get("productModel");

		try {
			// IPC流通道信息入库
			streamResourcesService.insertOrUpdateIPCameraInfo(xmlMap.get("sn"), "MAIN");
		} catch (Exception e) {
			logger.error("********IPC流通道信息新增或者更新失败失败********", e);
		}
		// 当产品是HPC03之类的p2p产品就不进行流资源调度
		try {
			if (!Constants.ipcModelType.isHPC03IPC(productModel)) {
				sReturn = streamResourcesService.StreamResourceSchedule(xmlMap);
			} else {
				logger.info("********产品是HPC03之类的p2p产品,所以不进行流资源调度********");
				sReturn = sb.toString();
			}
		} catch (Exception e) {
			logger.error("********流资源调度失败********", e);
			sReturn = "<?xml version=\"1.0\" encoding=\"utf-8\"?><ipcCommand><status>1</status></ipcCommand>";
		}
		return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}
	
    /**
	 * 流服务子系统确认视频流是否过期
	 * @param id
	 * @param model
	 */
	@RequestMapping("/cloud/sub/streaming/snDevice/authentication/{id}.sub")
	@ResponseName("authentication")
	public void checkLiveUrlValidity(@PathVariable String id, SKLModel model) {

		int isExpire = streamResourcesService.checkLiveUrlValidity(id);
		model.addAttribute("isExpire", isExpire);
	}
	
	/**
      * parseIpcInstructionCenterRequest(解析来自IPC指令中心的请求报文)
      * @Title: parseIpcInstructionCenterRequest
      * @param  inputStream
      * @throws Exception (参数说明)
      * @return Map<String,String> (返回值说明)
      * @author wangming
      * @date 2015年12月24日
     */
    private Map<String, String> parseIpcInstructionCenterRequest(InputStream inputStream) throws Exception {

        Map<String, String> xmlMap = new HashMap<String, String>();
        NodeList nodeList = this.getXmlNodeList(inputStream);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node instanceof Element) {
                Element element = (Element) nodeList.item(i);
                String sTagName = element.getTagName();
                String sValue = element.getTextContent();
                if (sTagName.equals("sn")) {
                    xmlMap.put("sn", sValue);
                } else if (sTagName.equals("productModel")) {
                    xmlMap.put("productModel", sValue);
                } else if (sTagName.equals("connectStatus")) {
                    xmlMap.put("connectStatus", sValue);
                }
            }
        }
        return xmlMap;
    }
    
    /***
     * 获取xml的NodeList
     * @param inputstream
     * @return
     * @throws Exception
     */
    private NodeList getXmlNodeList(InputStream inputstream) throws Exception {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = docBuilderFactory.newDocumentBuilder();
        Document document = builder.parse(inputstream);
        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        return nodeList;
    }
    
}
