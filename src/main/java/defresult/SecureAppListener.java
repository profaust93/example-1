package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
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
    public void onMessage(Message message) {
        executor.submit(() -> {
            String correlationid = message.getMessageProperties().getCorrelationIdString();
            String response = service.method(new String(message.getBody()));
            CorrelationData correlationData = new CorrelationData(correlationid);
            template.convertAndSend("toApiApp", "*", response, correlationData);
        });
    }
}
