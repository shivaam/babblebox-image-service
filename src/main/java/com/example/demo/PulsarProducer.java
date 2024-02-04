package com.example.demo;

import org.apache.pulsar.client.api.PulsarClientException;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Component;

@Component
public class PulsarProducer {

    private final PulsarTemplate<String> pulsarTemplate;

    public PulsarProducer(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void someMethod() throws PulsarClientException {
        this.pulsarTemplate.send("persistent://babblebox/audio_processing/test", "Hello");
    }
}
