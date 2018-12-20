package com.lizeuw.mqtt.mqtt;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMqttApplication {
	
	@Autowired
	private MqttGateway mqttGateway;
	
	@Autowired
	private TopicReceiver topicReceiver;
	@Before
	public void tetMqttApplication() 
	{
		mqttGateway.sendToMqtt("hello world", "topic");
	}
	
	
}
