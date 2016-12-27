package defresult;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;


public class MyService {
    @Autowired
    AmqpTemplate template;


}
