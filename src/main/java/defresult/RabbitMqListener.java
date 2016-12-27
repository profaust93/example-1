package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
@Component
public class RabbitMqListener {
    Logger logger = Logger.getLogger(RabbitMqListener.class);

    public static Map<String, Message> map = new HashMap<>();

    @Autowired
    RabbitTemplate template;

    public int counter = 0;


    @RabbitListener(queues = "queue1")
    public void processQueue1(Message message) {
        logger.info("Received from queue 1: " + new String(message.getBody()));
        byte[] msg = message.getBody();
        byte[] corrId = message.getMessageProperties().getCorrelationId();
        String ans = new String(msg) + " its my world" + " " + counter;
        Message nMsg = MessageBuilder.withBody(ans.getBytes()).setCorrelationId(corrId).build();
        template.convertAndSend("queue2", nMsg);
        counter++;


    }

    @RabbitListener(queues = "queue2")
    public void processQueue2(Message message) {
        byte[] corrId = message.getMessageProperties().getCorrelationId();
        System.out.println("Putting Key:" + new String (corrId) + " Value: " + new String (message.getBody()));
        map.put(new String(corrId), message);
        logger.info("Received from queue 2: " + new String(message.getBody()));


    }

    public Map<String, Message> getMap() {
        return map;
    }
}
