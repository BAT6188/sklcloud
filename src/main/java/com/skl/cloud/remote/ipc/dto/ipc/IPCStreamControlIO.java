package com.skl.cloud.remote.ipc.dto.ipc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
  * @ClassName: IPCSteamControlIO
  * @Description: TODO
  * @author wangming
  * @date 2015年12月7日
  *
 */
@XmlRootElement(name = "streamControl")
@XmlAccessorType(XmlAccessType.FIELD)
public class IPCStreamControlIO {

    private String id;
    private String control;
    private String controlSource;
    private String channelId;
    private String protocolType;

    @XmlElement(name = "receiverAddress")
    private ReceiverAddress receiverAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

    public String getControlSource() {
        return controlSource;
    }

    public void setControlSource(String controlSource) {
        this.controlSource = controlSource;
    }

    public String getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(String protocolType) {
        this.protocolType = protocolType;
    }

    public ReceiverAddress getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(ReceiverAddress receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

}
