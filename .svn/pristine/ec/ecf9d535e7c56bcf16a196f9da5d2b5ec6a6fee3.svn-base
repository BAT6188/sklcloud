package com.skl.cloud.controller.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.skl.cloud.service.common.DigestService;
import com.skl.cloud.service.common.IPCInstructionCenterService;

/**
 * 
 * @ClassName: IPCInstructionCenterController
 * @Description: IPC指令中心
 * @author wangming
 * @date 2015年8月18日
 *
 */
@Controller
@RequestMapping("/skl-cloud/appremote")
public class IPCInstructionCenterController extends BaseController {
	private static final Logger logger = Logger.getLogger(IPCInstructionCenterController.class);

	@Autowired(required = true)
	private IPCInstructionCenterService iPCInstructionCenterService;
	
	@Autowired
	private DigestService digestService;

	/**
	 * receiveAppRequest(接收APP的指令请求)
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param request
	 * @param (参数说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("参数1");
		return sendIPCInstructionCenter(request, response, SN);

		// return new ResponseEntity<String>(sReturn, HttpStatus.OK);
	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param @param SN
	 * @param @param ipcUri1
	 * @param @param ipcUri2
	 * @param @param request
	 * @param @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}")
	public ResponseEntity<byte[]> receiveAppRequest3(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, HttpServletRequest request, HttpServletResponse response) {
		System.out.println("参数2");
		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param @param SN
	 * @param @param ipcUri1
	 * @param @param ipcUri2
	 * @param @param ipcUri3
	 * @param @param request
	 * @param @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("参数3");
		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param @param SN
	 * @param @param ipcUri1
	 * @param @param ipcUri2
	 * @param @param ipcUri3
	 * @param @param ipcUri4
	 * @param @param request
	 * @param @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("参数4");
		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param @param SN
	 * @param @param ipcUri1
	 * @param @param ipcUri2
	 * @param @param ipcUri3
	 * @param @param ipcUri4
	 * @param @param ipcUri5
	 * @param @param request
	 * @param @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, HttpServletRequest request, HttpServletResponse response) {

		System.out.println("参数5");
		return sendIPCInstructionCenter(request, response, SN);
	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param ipcUri2
	 * @param ipcUri3
	 * @param ipcUri4
	 * @param ipcUri5
	 * @param ipcUri6
	 * @param request
	 * @param (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}/{ipcUri6}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, @PathVariable String ipcUri6, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("参数6");
		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param ipcUri2
	 * @param ipcUri3
	 * @param ipcUri4
	 * @param ipcUri5
	 * @param ipcUri6
	 * @param ipcUri7
	 * @param request
	 * @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}/{ipcUri6}/{ipcUri7}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, @PathVariable String ipcUri6, @PathVariable String ipcUri7,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("参数7");
		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param ipcUri2
	 * @param ipcUri3
	 * @param ipcUri4
	 * @param ipcUri5
	 * @param ipcUri6
	 * @param ipcUri7
	 * @param ipcUri8
	 * @param request
	 * @return ResponseEntity<String> (返回值说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}/{ipcUri6}/{ipcUri7}/{ipcUri8}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, @PathVariable String ipcUri6, @PathVariable String ipcUri7,
			@PathVariable String ipcUri8, HttpServletRequest request, HttpServletResponse response) {

		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param ipcUri2
	 * @param ipcUri3
	 * @param ipcUri4
	 * @param ipcUri5
	 * @param ipcUri6
	 * @param ipcUri7
	 * @param ipcUri8
	 * @param ipcUri9
	 * @param request
	 * @param (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}/{ipcUri6}/{ipcUri7}/{ipcUri8}/{ipcUri9}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, @PathVariable String ipcUri6, @PathVariable String ipcUri7,
			@PathVariable String ipcUri8, @PathVariable String ipcUri9, HttpServletRequest request,
			HttpServletResponse response) {

		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * receiveAppRequest(接收APP的指令请求)
	 * 
	 * @Title: receiveAppRequest
	 * @Description: 接收APP的指令请求
	 * @param SN
	 * @param ipcUri1
	 * @param ipcUri2
	 * @param ipcUri3
	 * @param ipcUri4
	 * @param ipcUri5
	 * @param ipcUri6
	 * @param ipcUri7
	 * @param ipcUri8
	 * @param ipcUri9
	 * @param ipcUri10
	 * @param request
	 * @return (参数说明)
	 * @return ResponseEntity<String> (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	@RequestMapping("/{SN}/{ipcUri1}/{ipcUri2}/{ipcUri3}/{ipcUri4}/{ipcUri5}/{ipcUri6}/{ipcUri7}/{ipcUri8}/{ipcUri9}/{ipcUri10}")
	public ResponseEntity<byte[]> receiveAppRequest(@PathVariable String SN, @PathVariable String ipcUri1,
			@PathVariable String ipcUri2, @PathVariable String ipcUri3, @PathVariable String ipcUri4,
			@PathVariable String ipcUri5, @PathVariable String ipcUri6, @PathVariable String ipcUri7,
			@PathVariable String ipcUri8, @PathVariable String ipcUri9, @PathVariable String ipcUri10,
			HttpServletRequest request, HttpServletResponse response) {

		return sendIPCInstructionCenter(request, response, SN);

	}

	/**
	 * 
	 * sendIPCInstructionCenter(把APP到业控的指令转发到IPC指令中心子系统)
	 * 
	 * @Title: sendIPCInstructionCenter
	 * @Description: 把APP到业控的指令转发到IPC指令中心子系统
	 * @param request
	 * @param SN
	 * @param (参数说明)
	 * @return String (返回值说明)
	 * @throws (异常说明)
	 * @author wangming
	 * @date 2015年8月20日
	 */
	public ResponseEntity<byte[]> sendIPCInstructionCenter(HttpServletRequest request, HttpServletResponse response,
			String SN) {
		return iPCInstructionCenterService.sendIPCInstructionCenter(request, response, SN);
	}

}
