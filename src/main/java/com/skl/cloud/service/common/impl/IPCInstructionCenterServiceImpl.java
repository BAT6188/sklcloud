package com.skl.cloud.service.common.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.druid.util.StringUtils;
import com.skl.cloud.common.exception.ManagerException;
import com.skl.cloud.dao.common.IPCInstructionCenterMapper;
import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.service.common.IPCInstructionCenterService;
import com.skl.cloud.util.common.StringUtil;
import com.skl.cloud.util.common.XmlUtil;
import com.skl.cloud.util.http.HttpRequest;

@Service("iPCInstructionCenterService")
public class IPCInstructionCenterServiceImpl implements IPCInstructionCenterService {
    private static final Logger logger = Logger.getLogger(IPCInstructionCenterServiceImpl.class);
    
    @Autowired(required = true)
    private IPCInstructionCenterMapper iPCInstructionCenterMapper;
    
    @Autowired
    private DigestService digestService;
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> queryIPCInstructionInfo(String sn) throws ManagerException {

        return iPCInstructionCenterMapper.queryIPCInstructionInfo(sn);
    }
    
    /**
     * 
     * sendIPCInstructionCenter(把APP到业控的指令转发到IPC指令中心子系统)
     * 
     * @Title: sendIPCInstructionCenter
     * @Description: 把APP到业控的指令转发到IPC指令中心子系统
     * @param @param request
     * @param @param SN
     * @param @return (参数说明)
     * @return String (返回值说明)
     * @throws (异常说明)
     * @author wangming
     * @date 2015年8月20日
     */
    @Override
    @Transactional
    @SuppressWarnings("restriction")
    public ResponseEntity<byte[]> sendIPCInstructionCenter(HttpServletRequest request, HttpServletResponse response,
            String SN) {
        String sReturn = "";

        logger.info("sn:" + SN);
        // 验证SN是否为空
        if (StringUtils.isEmpty(SN)) {
            sReturn = XmlUtil.mapToXmlError("0x50000047");
            return returnXml(response, sReturn);
        }
       

       String requestMethod = request.getMethod();
        logger.info("RequestMethod:" + requestMethod);

        // 获取APP请求的Uri
        String requestURI = request.getRequestURI();
        logger.info("app请求的url：" + requestURI);

        int snIndex = requestURI.indexOf(SN);
        String str = requestURI.substring(snIndex);

        // 获取sn后面的IpcUri
        String IPCUri = str.substring(SN.length());
        SN = StringUtil.strTrim(SN);
        // 通过Sn查询对应的IPC指令中心子系统的ip地址和端口号。
        Map<String, Object> iPCInstructionInfo = this.queryIPCInstructionInfo(SN);

        if (iPCInstructionInfo == null) {
            logger.warn("The ip dose not existe in the database By looking for SN");
            sReturn = XmlUtil.mapToXmlError("0x50010019");
            return returnXml(response, sReturn);

        }
        String ipcIp = (String) iPCInstructionInfo.get("Ip");
        // 不从数据库获取，写死在代码中
        int ipcPort = 29098;

        // 组装成新的URi
        String sendIPCCenterURL = "http://" + ipcIp + ":" + ipcPort + "/skl-cloud/appremote/" + SN + IPCUri;

        @SuppressWarnings("unused")
        InputStream ipcInstructionCenterIs = null;
        
        // 转发APP请求到指令中心
        String sendXml = this.fetchRequestHeaders(SN, request).toString();
        
        String allResponse;
        try { 
            //计算延时
            Date beginDate = new java.util.Date();
            logger.info("********业控开始请求指令中心的时间是："+beginDate);
            long beginTime = beginDate.getTime();
            
            allResponse = HttpRequest.getInstance().doPost(sendIPCCenterURL, "utf-8", sendXml);
 
            Date endDate = new java.util.Date();
            logger.info("********指令中心响应业控的时间是："+endDate);
            long endTime = endDate.getTime();
            logger.info("******业控从请求到接收到指令中心响应的时间间隔是："+(endTime-beginTime)+"毫秒");
            
            //logger.info("********IPC指令中心响应的信息是："+allResponse +"********");
            
            if ("".equals(allResponse) || allResponse == null) {
            	logger.warn("********IPC指令中心返回的信息是空，请求失败********");
                sReturn = XmlUtil.mapToXmlError("0x50010013");
                return returnXml(response, sReturn);
            }
            Map responseMap = XmlUtil.getElementMapCommand(allResponse);
            String body = (String) responseMap.get("body");
            
            byte[] bt = Base64.decodeBase64(body);
        
            String sCt = (String) responseMap.get("Content-Type"); // 响应Content-Type
            response.setContentType(sCt);
            
            Map<String, Object> headerMap = (Map<String, Object>) responseMap.get("headerMap");
            for (Entry<String, Object> entry : headerMap.entrySet()) {
                response.setHeader(entry.getKey(), (String) entry.getValue());
            }
            
            // 输入完毕，清楚缓冲
            //out.flush();
            return new ResponseEntity<byte[]>(bt, HttpStatus.OK);
        } catch (Exception e) {
        	logger.error("请求IPC失败,msg:"+e.getMessage(), e);
            sReturn = XmlUtil.mapToXmlError("0x50010001");
            return returnXml(response, sReturn);
        }
    }

    private ResponseEntity<byte[]> returnXml(HttpServletResponse response, String xml) {
        response.setContentType("text/xml");
        return new ResponseEntity<byte[]>(xml.getBytes(), HttpStatus.OK);
    }
    
    /**
     * 进行Header进行处理
     * 
     * @param request
     * @return
     * @throws IOException 
     */
    private StringBuilder fetchRequestHeaders(String sn, HttpServletRequest request) {
        StringBuilder headersXml = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        headersXml.append("<request>");
        headersXml.append("<httpCmd>");
        headersXml.append("<method>");
        headersXml.append(request.getMethod());
        headersXml.append("</method>");
        headersXml.append("<uri>");
        //get uri
        String reqUri = request.getRequestURI();
        CharSequence subURL =reqUri.subSequence(reqUri.indexOf("appremote/"), reqUri.length());
        int len = subURL.toString().indexOf("/", "appremote/".length());
        CharSequence uri = subURL.toString().subSequence(len, subURL.length());
        headersXml.append(uri);
        headersXml.append("</uri>");
        headersXml.append("<httpVersion>");
        headersXml.append(request.getProtocol());
        headersXml.append("</httpVersion>");
        headersXml.append("</httpCmd>");
        headersXml.append("<headers>");

        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = (String) headerNames.nextElement();
            if(StringUtils.equals("Authorization", headerName)) {
                continue;
            }
            else {
                Enumeration<String> headValues = request.getHeaders(headerName);
                while (headValues.hasMoreElements()) {
                    String headerValue = headValues.nextElement();
                    headersXml.append("<header>");
                    headersXml.append("<name>").append(headerName).append("</name>");
                    headersXml.append("<value>").append(headerValue).append("</value>");
                    headersXml.append("</header>");
                }
            }
        }
        // 加入IPC Digest
        String authorization = digestService.getIPCDigest(sn);
        headersXml.append("<header><name>Authorization</name><value>" + authorization + "</value></header>");

        headersXml.append("</headers>");
        headersXml.append("<body>");
        
        //inputStream to byte
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        //buff用于存放循环读取的临时数据
        byte[] buff = new byte[100]; 
        int rc = 0;
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            while ((rc = inputStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, rc);
            }
        } catch (IOException e) {
            logger.error("read inputStream error"+e.getMessage(), e);
        }
        byte[] in_b = swapStream.toByteArray();
        String encodeBody = Base64.encodeBase64String(in_b);
        
        /*byte[] ints = new byte[4];
        int num = -1;
        if(in_b.length >= 4){
            System.arraycopy(in_b, 0, ints, 0, ints.length);
            num = bytesToInt(ints);
        }*/
        logger.info("-------reqUri--------"+reqUri);
        //logger.info("-------num--------"+num);
        //logger.info("-------in_b--------"+in_b.length);
        //logger.info("-------encodeBody--------"+encodeBody.length());
        //logger.info("***************发给IPC指令中心编码后的body："+encodeBody);
        
        headersXml.append(encodeBody);
        headersXml.append("</body>");
        headersXml.append("</request>");
        return headersXml;
    }
    
    
    /**
     * 基于位移的byte[]转化成int 
     * @param bytes
     * @return int
     */
    public static int bytesToInt(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }
    
}
