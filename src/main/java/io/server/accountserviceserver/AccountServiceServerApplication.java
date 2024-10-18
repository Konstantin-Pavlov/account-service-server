package io.server.accountserviceserver;

import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class AccountServiceServerApplication {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private List<Queue> allQueues;

    @PostConstruct
    public void declareQueue() {
        for (Queue queue : allQueues) {
            rabbitAdmin.declareQueue(queue);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(AccountServiceServerApplication.class, args);
    }

}
