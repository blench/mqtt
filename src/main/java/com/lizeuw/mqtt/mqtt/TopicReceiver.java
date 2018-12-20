package com.lizeuw.mqtt.mqtt;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicReceiver {
    
	/**
	 * ָ������
	 * @param msg
	 * @return
	 */
    @RabbitListener(queues = "rabbitopic")
    public void processMessage1(String msg) {
//    	Message message = rabbitTemplate.receive(10000);
        System.out.println(" ���յ�����rabbitopic���е���Ϣ��" + msg);
        return;
    }
    
    @RabbitListener(queues = "rabbitopic.queue2")
    public void processMessage2(String msg) {
        System.out.println(Thread.currentThread().getName() + " ���յ�����rabbitopic.queue2���е���Ϣ��" + msg);
        return;
    }
}