package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.jms.QueueReceiver;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Controller
public class SampleController {
    Logger logger = Logger.getLogger(SampleController.class);

    @Autowired
    RabbitTemplate template;

    Integer count = 0;


    @RequestMapping("/emit")
    @ResponseBody
    DeferredResult<ResponseEntity<Object>> queue1() throws InterruptedException, ExecutionException, TimeoutException {
        DeferredResult response = new DeferredResult();
        Object fromService = CompletableFuture
                .supplyAsync(this::sendMsg)
                .thenAcceptAsync(this::receiveMsg)
                .get(3, TimeUnit.SECONDS);
        String answer = (String)fromService;
        System.out.println("ANSWER: " + answer);
        response.setResult(new ResponseEntity<>(fromService, HttpStatus.OK));
        return response;
    }

    String sendMsg() {
        String msg = "Hello World";

        String corrId = UUID.randomUUID().toString();    //count.toString();
        logger.info("Sending key: " + corrId);
        Message message = MessageBuilder.withBody(msg.getBytes()).setCorrelationId(corrId.getBytes()).build();
        template.convertAndSend("queue1", message);
        count++;
        return corrId;
    }

    Message receiveMsg(String correlationId) {
//        System.out.println("Size: " + listener.getMap().size() + " Content: " + new String(listener.getMap().get(correlationId).getBody()));

        return RabbitMqListener.map.get(correlationId);
    }


}