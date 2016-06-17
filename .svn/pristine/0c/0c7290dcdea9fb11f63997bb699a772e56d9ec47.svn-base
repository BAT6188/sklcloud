package com.skl.cloud.foundation.remote.stream;

import com.skl.cloud.foundation.remote.HttpRemoteContext;
import com.skl.cloud.foundation.remote.RemoteContext;

public class StreamRemoteContext extends HttpRemoteContext {
    // 根节点名称
    private String rootName;
    // xmlns
    private String xmlns;
    // stream type
    private String streamType;

    @Override
    public void merge(RemoteContext context) {
        super.merge(context);
        if (context instanceof StreamRemoteContext) {
            StreamRemoteContext ipcContext = (StreamRemoteContext) context;
            if (ipcContext.getRootName() != null) {
                setRootName(ipcContext.getRootName());
            }
            if (ipcContext.getXmlns() != null) {
                setXmlns(ipcContext.getXmlns());
            }
        }
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

    /**
     * @param streamType 
     */
    public String getStreamType() {
        return streamType;
    }

    /**
     * @param streamType the streamType to set
     */
    public void setStreamType(String streamType) {
        this.streamType = streamType;
    }

}
