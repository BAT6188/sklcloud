package com.skl.cloud.service.ipc;


public interface RabbitMqPublishService {

    /**
      * 把IPC告警信息发送到前置的MQ中
      * @param obj
     */
    public void sendIPCDetectionToFrontQueue(Object obj);
}