package com.example.demo;

import ch.qos.logback.core.net.SyslogOutputStream;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.common.schema.SchemaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class PulsarConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PulsarConsumer.class);
    private static final AtomicInteger messageCount = new AtomicInteger(0);
     AudioMessageService audioMessageService = new AudioMessageService();
     Dalle dalle = new Dalle();

    @Autowired
    public PulsarConsumer() {
    }

//    @PulsarListener(topics = "persistent://babblebox/audio_processing/new_transcription",
//            subscriptionType = SubscriptionType.Exclusive,
//            schemaType = SchemaType.AVRO
//    )
    public void processMessage(ChatMessage chatMessage) {
        System.out.println(chatMessage.toString());
        String audio_message_id = chatMessage.getAudioMessageId().toString();
        String imageId = chatMessage.getImageId().toString();
        String transcription = audioMessageService.getAudioMessageTranscription(audio_message_id);
        String imagePath = dalle.generateImage(transcription);
        dalle.uploadImage2(imagePath, imageId);
        System.out.println("Total messages processed: " + Integer.toString(PulsarConsumer.messageCount.incrementAndGet()));
    }

    public static void main(String[] args) {
        PulsarConsumer pulsarConsumer = new PulsarConsumer();
        String audio_message_id = "ae89dd51-b683-48b7-aa2d-6bac9e768c97";
        String imageid = "42037c35-262c-4bf6-88bc-2d3d3a0ad524";
        String transcription = pulsarConsumer.audioMessageService.getAudioMessageTranscription(audio_message_id);
        String imagePath = pulsarConsumer.dalle.generateImage(transcription);
        pulsarConsumer.dalle.uploadImage2(imagePath, imageid);
    }
}
