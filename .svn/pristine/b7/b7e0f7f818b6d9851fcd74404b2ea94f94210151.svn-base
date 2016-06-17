package com.skl.cloud.service.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

import com.skl.cloud.common.exception.ManagerException;

public interface IPCInstructionCenterService {
    public Map<String, Object> queryIPCInstructionInfo(String sn) throws ManagerException;
    
    public ResponseEntity<byte[]> sendIPCInstructionCenter(HttpServletRequest request, HttpServletResponse response,
            String SN);

}
