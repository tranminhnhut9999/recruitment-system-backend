package project.springboot.template.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // Exchange Names
    public static final String EXCHANGE_NAME = "eventExchange";
    // Queue Names
    public static final String NEW_APPLICATION_QUEUE = "newApplicationQueue";
    public static final String NEW_APPLICATION_RK = "newApplicationRK";
    // Routing Keys
    public static final String GENERAL_EVENT_QUEUE = "generalEventQueue";

    // Define Exchange
    @Bean
    public TopicExchange eventExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Define Specific Event Queue
    @Bean
    public Queue newApplicationEventQueue() {
        return new Queue(NEW_APPLICATION_QUEUE);
    }

    // Define General Event Queue
    @Bean
    public Queue generalEventQueue() {
        return new Queue(GENERAL_EVENT_QUEUE);
    }

    // Define Bindings
    @Bean
    public Binding specificEventBinding() {
        return BindingBuilder.bind(newApplicationEventQueue()).to(eventExchange()).with(NEW_APPLICATION_RK);
    }

    @Bean
    public Binding generalEventBinding() {
        return BindingBuilder.bind(generalEventQueue()).to(eventExchange()).with("event.general");
    }

}