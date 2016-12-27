package defresult;

import org.apache.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.*;

@Controller
public class SampleController {
    Logger logger = Logger.getLogger(SampleController.class);

    @Autowired
    RabbitTemplate template;

    Integer count = 0;

    @Resource(name = "receivedMessages")
    ConcurrentHashMap<String, Object> receivedMessages;



    @RequestMapping("/emit")
    @ResponseBody
    DeferredResult<ResponseEntity<Object>> queue1() throws InterruptedException, ExecutionException, TimeoutException {
        DeferredResult response = new DeferredResult();
        String id = sendMsg();
        String answer = (String) receiveMsg(id);
        System.out.println("ANSWER: " + answer);
        response.setResult(new ResponseEntity<>(answer, HttpStatus.OK));
        return response;
    }

    String sendMsg() {
        String msg = "Hello World";

        String corrId = UUID.randomUUID().toString();    //count.toString();
        logger.info("Sending key: " + corrId);
        CorrelationData correlationData = new CorrelationData(corrId);
        Map<String, Object> header = new HashMap<>();
        header.put("correlationId", corrId);
        Message<String> message = new GenericMessage(msg, header);
        template.convertAndSend("toSecureApp", "*", message);
        count++;
        return corrId;
    }

    Object receiveMsg(String correlationId){
        while (true){
              if (receivedMessages.containsKey(correlationId)){
                  return receivedMessages.get(correlationId);
              }
              try {
                  TimeUnit.MILLISECONDS.sleep(10);
              } catch (InterruptedException e){
                  Thread.interrupted();
              }
        }
    }


}