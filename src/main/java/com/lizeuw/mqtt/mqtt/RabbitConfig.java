package com.lizeuw.mqtt.mqtt;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitConfig implements RabbitListenerConfigurer {
    
    //��������
    @Bean
    public Queue queue1() {
        return new Queue("rabbitopic", true); // true��ʾ�־û��ö���
    }
    
    @Bean
    public Queue queue2() {
        return new Queue("rabbitopic.queue2", true);
    }
    
    //����������
    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    //��
    @Bean
    public Binding binding1() {
        return BindingBuilder.bind(queue1()).to(topicExchange()).with("key.1");
    }
    
    @Bean
    public Binding binding2() {
        return BindingBuilder.bind(queue2()).to(topicExchange()).with("key.#");
    }
   
    @Bean
    public DefaultMessageHandlerMethodFactory myHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
        factory.setMessageConverter(new MappingJackson2MessageConverter());
        return factory;
    }

    //queue listener �۲� ����ģʽ ������Ϣ����ʱ��֪ͨ�����ڶ�Ӧ�Ķ����ϵļ�������
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        // factory.setPrefetchCount(5);//ָ��һ�������ܴ�����ٸ���Ϣ�����������Ļ���������ڵ���transaction����.
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        //MANUAL����ACK�޸�Ϊ�ֶ�ȷ�ϣ�������Ϣ�ڴ�������з����쳣��ɱ�����Ϊ�Ѿ��ɹ����ѵļ���
        //factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return factory;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
        registrar.setMessageHandlerMethodFactory(myHandlerMethodFactory());
    }
}
