package com.example.demo;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AudioMessageService {

    private RestTemplate restTemplate;
    String apiAudioUrl = "https://babblebox-app.shivamrastogi.com/api/AudioFile";

    public AudioMessageService()  {
        this.restTemplate = AudioMessageService.getRestTemplate();
    }

    public static RestTemplate getRestTemplate() {
//        try {
//            final TrustStrategy acceptingTrustStrategy = (cert, authType) -> true;
//            final SSLContext sslContext = SSLContexts.custom()
//                    .loadTrustMaterial(null, acceptingTrustStrategy)
//                    .build();
//            final SSLConnectionSocketFactory sslsf =
//                    new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
//            final Registry<ConnectionSocketFactory> socketFactoryRegistry =
//                    RegistryBuilder.<ConnectionSocketFactory>create()
//                            .register("https", sslsf)
//                            .register("http", new PlainConnectionSocketFactory())
//                            .build();
//
//            final BasicHttpClientConnectionManager connectionManager =
//                    new BasicHttpClientConnectionManager(socketFactoryRegistry);
//
//            CloseableHttpClient httpClient = HttpClients.custom()
//                    .setConnectionManager(connectionManager)
//                    .build();
//            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
//            requestFactory.setHttpClient(httpClient);
//            RestTemplate restTemplate = new RestTemplate(requestFactory);
//            return restTemplate;
//        } catch(Exception e) {
//
//        }
        return new RestTemplate();
    }

    // Function to get the audio message ID
    public String getAudioMessageTranscription(String audioMessageId) {
        String url = apiAudioUrl + "/" + audioMessageId + "/";

        // Create headers and add the authentication token
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Token " + "380915803ba47517c0e0dc21add9c814f85e9dd4");  // Assuming 'Token' as the token prefix
        HttpEntity entity = new HttpEntity("body", headers);


        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

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
//    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//        RestClientConfig.ignoreCertificates();
//        AudioMessageService service = new AudioMessageService();
//        String transcription = service.getAudioMessageTranscription("8898f450-c34f-4507-bf87-e195b4362787");
//        System.out.println(transcription);
//    }
}
