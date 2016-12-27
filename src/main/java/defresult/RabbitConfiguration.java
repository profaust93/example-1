package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;

public class RabbitConfiguration {
    Logger logger = Logger.getLogger(RabbitConfiguration.class);

    //настраиваем соединение с RabbitMQ
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        return connectionFactory;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    //объявляем очередь с именем queue1

//    //объявляем контейнер, который будет содержать листенер для сообщений
//    @Bean
//    public SimpleMessageListenerContainer messageListenerContainer1() {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory());
//        container.setQueueNames("queue1");
//        container.setMessageListener((MessageListener) message -> rabbitTemplate().convertAndSend("queue2", message));
//        return container;
//    }
//
////    @Bean
////    public SimpleMessageListenerContainer messageListenerContainer2() {
////        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
////        container.setConnectionFactory(connectionFactory());
////        container.setQueueNames("queue2");
////        container.setMessageListener((MessageListener) message -> logger.info("received from queue2 : " + new String(message.getBody())));
////        return container;
////    }
}