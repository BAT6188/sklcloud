package com.skl.cloud.foundation.remote.ipc;

import com.skl.cloud.foundation.remote.HttpRemoteContext;
import com.skl.cloud.foundation.remote.RemoteContext;

public class IPCRemoteContext extends HttpRemoteContext {
	// sn
	private String sn;
	// 根节点名称
	private String rootName;
	// xmlns
	private String xmlns;
	

	@Override
	public void merge(RemoteContext context) {
		super.merge(context);
		if(context instanceof IPCRemoteContext) {
			IPCRemoteContext ipcContext = (IPCRemoteContext)context;
			if(ipcContext.getSn() != null) {
				setSn(ipcContext.getSn());
			}
			if(ipcContext.getRootName() != null) {
				setRootName(ipcContext.getRootName());
			}
			if(ipcContext.getXmlns() != null) {
				setXmlns(ipcContext.getXmlns());
			}
		}
	}
	
	/**
	 * @return the sn
	 */
	public String getSn() {
		return sn;
	}
	/**
	 * @param sn the sn to set
	 */
	public void setSn(String sn) {
		this.sn = sn;
	}
	/**
	 * @return the rootName
	 */
	public String getRootName() {
		return rootName;
	}
	/**
	 * @param rootName the rootName to set
	 */
	public void setRootName(String rootName) {
		this.rootName = rootName;
	}
	/**
	 * @return the xmlns
	 */
	public String getXmlns() {
		return xmlns;
	}
	/**
	 * @param xmlns the xmlns to set
	 */
	public void setXmlns(String xmlns) {
		this.xmlns = xmlns;
	}

}
