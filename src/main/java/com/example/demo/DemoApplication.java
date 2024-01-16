package com.example.demo;

import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args);

        PulsarProducer pulsarProducer = context.getBean(PulsarProducer.class);
        // Use the MyBean instance to send a message
        try {

            pulsarProducer.someMethod();
            System.out.println("Message sent successfully.");
        } catch (PulsarClientException e) {
            System.err.println("Failed to send message: " + e.getMessage());
        }
    }
}
