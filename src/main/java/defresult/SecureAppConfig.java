package defresult;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by User on 27.12.2016.
 */
@Configuration
public class SecureAppConfig {

    @Bean
    public Queue toApiAppQ() {
        return new Queue("toApiAppQ");
    }

    @Bean
    public TopicExchange toApiAppExchange(){
        return new TopicExchange("toApiApp");
    }

    @Bean
    public Binding toApiAppBinding(){
        return BindingBuilder.bind(toApiAppQ()).to(toApiAppExchange()).with("*");
    }

    @Bean(name = "secureAppExecutor")
    public ExecutorService secureAppExecutor(){
        return Executors.newFixedThreadPool(10);
    }

}
