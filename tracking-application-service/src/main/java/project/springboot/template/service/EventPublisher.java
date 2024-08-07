package project.springboot.template.service;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import project.springboot.template.config.RabbitMQConfig;

@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishEvent(String eventType, String messageContent) {
        System.out.println("PUBLISH EVENT:" + eventType);
        Message message = MessageBuilder.withBody(messageContent.getBytes())
                .setHeader("eventType", eventType)
                .build();
        rabbitTemplate.send(RabbitMQConfig.EXCHANGE_NAME, eventType, message);
    }
}
