package com.lizeuw.mqtt.mqtt;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

/*
**
 * ????仰?????????<br> 
 * ??MQTT??????????á?
 *
 * @author AnswerChang
 * @create 2018/6/4
 * @since 1.0.0
 */
@Configuration
@IntegrationComponentScan
public class MqttSenderConfig {
 
    @Value("${spring.mqtt.username}")
    private String username;
 
    @Value("${spring.mqtt.password}")
    private String password;
 
    @Value("${spring.mqtt.url}")
    private String hostUrl;
 
    @Value("${spring.mqtt.client.id}")
    private String clientId;
 
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;
 
    @Value("${spring.mqtt.completionTimeout}")
    private int completionTimeout ;   //??????
    
    @Autowired
    private TopicSender topicSender;
    
    @Bean
    public MqttConnectOptions getMqttConnectOptions(){
        MqttConnectOptions mqttConnectOptions=new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        return mqttConnectOptions;
    }
    
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }
  
    //mqttOutboundChannel
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler mqttOutbound() {
        MqttPahoMessageHandler messageHandler =  new MqttPahoMessageHandler(clientId, mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic(defaultTopic);
        return messageHandler;
    }
    
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }
    
  //???????
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    
  //????client,??????topic 
    @Bean
    public MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId+"_inbound", mqttClientFactory(),
                        "topic","hello");
        adapter.setCompletionTimeout(completionTimeout);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttOutboundChannel());
        return adapter;
    }
 
    //?????????????
  
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
//                String type = topic.substring(topic.lastIndexOf("/")+1, topic.length());
                if("hello".equalsIgnoreCase(topic)){
                    System.out.println("hello,fuckXX," + message.getPayload().toString());
                    topicSender.send(message.getPayload().toString());
                }else if("topic".equalsIgnoreCase(topic)){
                    System.out.println("topic,fuckXX," + message.getPayload().toString());
                    topicSender.send(message.getPayload().toString());
                }
            }
        };
    }

}
