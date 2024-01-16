package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MessagePrinter {
    private static final Logger logger = LoggerFactory.getLogger(MessagePrinter.class);

    public void printMessageAfterDelay(String message) {
        try {
            //logger.info("Handling Pulsar message in thread: " + Thread.currentThread().getName());
            Random random = new Random();
            int number = random.nextInt(2); // Generates a number between 0-50
            //System.out.println(message + ": Processing message for seconds: "+ number);

            // Wait for 15 seconds
            Thread.sleep(number*1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread was interrupted");
        }

        // Print the message
        System.out.println(message + ": Completed processing");
    }
}

