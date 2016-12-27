package defresult;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by User on 27.12.2016.
 */
@Configuration
public class ApiAppListener {

    @Autowired
    RabbitTemplate template;

    @Resource(name = "apiAppExecutor")
    ExecutorService executor;

    @Resource(name = "receivedMessages")
    ConcurrentHashMap<String, Object> receivedMessages;

    @RabbitListener(queues = "toApiAppQ")
    public void onMessage(Message<String> message){
        String id = (String) message.getHeaders().get("correlationId");
        receivedMessages.put(id, message.getPayload());
    }

}
