package com.example.demo;

import org.apache.avro.Schema;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.hc.core5.http.config.Lookup;

@Component
public class PulsarConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PulsarConsumer.class);
    private static final AtomicInteger messageCount = new AtomicInteger(0);
     AudioMessageService audioMessageService = new AudioMessageService();
     Dalle dalle = new Dalle();
    Schema schema = new Schema.Parser().parse(new File("./src/main/resources/chat_message_schema.avsc"));


    @Autowired
    public PulsarConsumer() throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    }

    @PulsarListener(
            topics = "persistent://babblebox/audio_processing/new_transcription",
            subscriptionType = SubscriptionType.Exclusive,
            schemaType = SchemaType.JSON
    )
    public void processMessage(ChatMessage chatMessage) {
        try {
            System.out.println(chatMessage);
            String audio_message_id = chatMessage.audio_message_id();
            String imageId = chatMessage.image_id();
            System.out.println("Received audio message: " + audio_message_id);
            System.out.println("Received iamge message: " + imageId);
            String transcription = audioMessageService.getAudioMessageTranscription(audio_message_id);
            String imagePath = dalle.generateImage(transcription, imageId);
            dalle.uploadImage2(imagePath, imageId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Total messages processed: " + Integer.toString(PulsarConsumer.messageCount.incrementAndGet()));
    }

    public void handlePulsarError(Throwable t, Message<?> msg, @Headers MessageHeaders headers) {
        // Log the error details
        System.err.println("Error handling message: " + msg.getMessageId());
        t.printStackTrace();
    }

//    public static void main(String[] args) throws IOException {
//        PulsarConsumer pulsarConsumer = new PulsarConsumer();
//        String audio_message_id = "ae89dd51-b683-48b7-aa2d-6bac9e768c97";
//        String imageId = "42037c35-262c-4bf6-88bc-2d3d3a0ad524";
//        String transcription = pulsarConsumer.audioMessageService.getAudioMessageTranscription(audio_message_id);
//        String imagePath = pulsarConsumer.dalle.generateImage(transcription, imageId);
//        pulsarConsumer.dalle.uploadImage2(imagePath, imageId);
//    }
}
