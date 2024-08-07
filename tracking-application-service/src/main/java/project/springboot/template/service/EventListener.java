package project.springboot.template.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import project.springboot.template.config.RabbitMQConfig;

import java.util.Map;

@Component
public class EventListener {

//    @RabbitListener(queues = RabbitMQConfig.NEW_APPLICATION_QUEUE)
//    public void handleSpecificEvent(@Payload String message, @Headers Map<String, Object> headers) {
//        System.out.println("Received specific event: " + message);
//    }
//
//    @RabbitListener(queues = RabbitMQConfig.GENERAL_EVENT_QUEUE)
//    public void handleGeneralEvent(@Payload String message, @Headers Map<String, Object> headers) {
//        System.out.println("Received general event: " + message);
//    }
}