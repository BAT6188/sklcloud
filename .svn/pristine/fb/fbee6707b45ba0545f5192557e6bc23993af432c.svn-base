package com.skl.cloud.service.ipc.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.skl.cloud.controller.web.dto.DetectionFO;
import com.skl.cloud.service.ipc.RabbitMqPublishService;

@Service
public class RabbitMqPublishServiceImpl implements RabbitMqPublishService {

    @Autowired
    private AmqpTemplate amqpTemplate;

    /**
     * 把IPC告警信息发送到前置的MQ中
     * @param obj / DetectionFO
    */
    @Override
    public void sendIPCDetectionToFrontQueue(Object obj) {
        if (obj instanceof DetectionFO) {
            DetectionFO data = (DetectionFO) obj;
            Map<String, DetectionFO> keyMap = new HashMap<String, DetectionFO>();
            keyMap.put("data", data);
            amqpTemplate.convertAndSend(keyMap);
        } else {
            amqpTemplate.convertAndSend(obj);
        }
    }

}