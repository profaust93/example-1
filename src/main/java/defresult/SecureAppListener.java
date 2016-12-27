package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

@EnableRabbit //нужно для активации обработки аннотаций @RabbitListener
@Configuration
public class SecureAppListener {
    Logger logger = Logger.getLogger(SecureAppListener.class);

    @Autowired
    RabbitTemplate template;

    @Resource(name = "secureAppExecutor")
    ExecutorService executor;

    @Autowired
    SecureAppService service;

    @RabbitListener(queues = "toSecureAppQ")
    public void onMessage(Message<String> message) {
        executor.submit(() -> {
            String correlationid = (String) message.getHeaders().get("correlationId");
            String response = service.method((String) message.getPayload());
            logger.info(response);
            Map<String, Object> header = new HashMap<>();
            header.put("correlationId", correlationid);
            Message responseMessage = new GenericMessage(response, header);
            template.convertAndSend("toApiApp", "*", responseMessage);
        });
    }
}
