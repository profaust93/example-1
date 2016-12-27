package defresult;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by User on 27.12.2016.
 */
@Configuration
public class ApiConfig {

    @Bean
    public Queue toSecureAppQ() {
        return new Queue("toSecureAppQ");
    }

    @Bean
    public TopicExchange toSecureAppExchange() {
        return new TopicExchange("toSecureApp");
    }

    @Bean
    public Binding roSecureAppBinding() {
        return BindingBuilder.bind(toSecureAppQ()).to(toSecureAppExchange()).with("*");
    }

    @Primary
    @Bean(name = "apiAppExecutor")
    public ExecutorService apiAppExecutor() {
        return Executors.newFixedThreadPool(10);
    }

    @Bean(name = "receivedMessages")
    public ConcurrentHashMap<String, Object> receivedMessages(){
        return new ConcurrentHashMap<>();
    }

}
