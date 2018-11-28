package com.zhuochi.hydream.entity;

import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MyMqttMessage {
    private String topic;
    private MqttMessage mqttMessage;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public MqttMessage getMqttMessage() {
        return mqttMessage;
    }

    public void setMqttMessage(MqttMessage mqttMessage) {
        this.mqttMessage = mqttMessage;
    }
}
