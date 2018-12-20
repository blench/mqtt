package com.lizeuw.mqtt.mqtt;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicSender{
    
    @Autowired
    private AmqpTemplate  rabbitTemplate;
    
    //������Ϣ������Ҫʵ���κνӿڣ����ⲿ���á�
    public void send(String msg){
        
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        System.out.println("��ʼ������Ϣ : " + correlationId + msg.toLowerCase());
        this.rabbitTemplate.convertAndSend("topicExchange", "key.1", msg);
    }
}