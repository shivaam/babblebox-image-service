package com.example.demo;
import org.apache.pulsar.client.api.Consumer;
import org.springframework.pulsar.listener.PulsarConsumerErrorHandler;
import org.apache.pulsar.client.api.Message;
import org.springframework.stereotype.Component;

@Component
public class CustomPulsarConsumerErrorHandler implements PulsarConsumerErrorHandler {

    @Override
    public boolean shouldRetryMessage(Exception exception, Message message) {
        return false;
    }

    @Override
    public void recoverMessage(Consumer consumer, Message message, Exception thrownException) {

    }

    @Override
    public Message currentMessage() {
        return null;
    }

    @Override
    public void clearMessage() {

    }
}