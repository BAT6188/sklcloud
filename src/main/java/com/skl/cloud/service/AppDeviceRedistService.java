package com.skl.cloud.service;

import java.io.InputStream;

import org.springframework.http.ResponseEntity;

import com.skl.cloud.common.exception.ManagerException;

public interface AppDeviceRedistService
{
	public ResponseEntity<String> register(InputStream inputstream) throws ManagerException;

	public ResponseEntity<String> registerBySN(InputStream inputstream) throws ManagerException;
}
