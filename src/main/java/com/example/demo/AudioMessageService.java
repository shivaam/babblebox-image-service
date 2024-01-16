package com.example.demo;

import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.util.Map;

public class AudioMessageService {

    private RestTemplate restTemplate;
    String apiAudioUrl = "http://127.0.0.1:8000/api/AudioFile";

    public AudioMessageService() {
        this.restTemplate = new RestTemplate();
    }

    // Function to get the audio message ID
    public String getAudioMessageTranscription(String audioMessageId) {
        String url = apiAudioUrl + "/" + audioMessageId + "/";

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> data = response.getBody();
            System.out.println(data);
            assert data != null;
            return (String) data.get("transcription_en");
        } else {
            throw new RuntimeException("Failed to get audio message ID. Status code: " + response.getStatusCode());
        }
    }
//
//    // Function to get the audio file link
//    public String getAudioFileLink(String apiUrl, String audioMessageId) {
//        String url = apiUrl + "/" + audioMessageId + "/";
//        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
//        if (response.getStatusCode().is2xxSuccessful()) {
//            Map<String, Object> data = response.getBody();
//            System.out.println(data);
//            return (String) data.get("audio");
//        } else {
//            throw new RuntimeException("Failed to get audio file link. Status code: " + response.getStatusCode());
//        }
//    }
//
//    public static void main(String[] args) {
//        AudioMessageService service = new AudioMessageService();
//        String apiChatUrl = "http://127.0.0.1:8000/api/ChatMessage/51b9665e-8eb6-4cc1-b482-12ff29423aae/";
//        String apiAudioUrl = "http://127.0.0.1:8000/api/AudioFile";
//
//        try {
//            // Get the audio message ID
//            String audioMessageId = service.getAudioMessageId(apiChatUrl);
//            System.out.println(audioMessageId);
//
//            // Get the audio file link
//            String audioFileLink = service.getAudioFileLink(apiAudioUrl, audioMessageId);
//            System.out.println("Audio File Link: " + audioFileLink);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
    public static void main(String[] args) {
        AudioMessageService service = new AudioMessageService();
        String transcription = service.getAudioMessageTranscription("ae89dd51-b683-48b7-aa2d-6bac9e768c97");
        System.out.println(transcription);
    }
}
