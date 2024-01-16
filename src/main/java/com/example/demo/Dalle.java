package com.example.demo;

import io.github.stefanbratanov.jvm.openai.CreateImageRequest;
import io.github.stefanbratanov.jvm.openai.Images;
import io.github.stefanbratanov.jvm.openai.ImagesClient;
import io.github.stefanbratanov.jvm.openai.OpenAI;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;


import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Collections;

public class Dalle {
    private final String apiToken = "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";

    OpenAI openAI = OpenAI.newBuilder(apiToken).build();

    public static void main(String[] args) throws IOException {
        Dalle dalle = new Dalle();
        String path = dalle.generateImage("I love to test different things and expoore life");
        dalle.uploadImage2(path, "42037c35-262c-4bf6-88bc-2d3d3a0ad524");
    }

    //Returns image path
    public String generateImage(String transcription) {
        ImagesClient imagesClient = openAI.imagesClient();
        CreateImageRequest createImageRequest = CreateImageRequest.newBuilder()
                .model("dall-e-3")
                .size("1024x1024")
                .responseFormat("b64_json")
                .prompt("Here is a conversation between two people. Generate an image that represents the idea behind this conversation. " +
                        "DO NOT INCLUDE any faces. Message starts after this prompt: " +
                        "Generate and otter image that is funny")
                .build();
        Images images = imagesClient.createImage(createImageRequest);
        byte[] encodedImageBytes = images.data().get(0).b64Json().getBytes();
        byte[] decodedImageBytes = Base64.getDecoder().decode(encodedImageBytes);

        String path = "downloadedImage.png";
        // Write to file
        try (FileOutputStream imageOutFile = new FileOutputStream(path)) { // Adjust file extension based on image format
            imageOutFile.write(decodedImageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Images[created=1704009569, data=[Image[b64Json=null, url=https://foo.bar/cute-baby-sea-otter.png, revisedPrompt=Generate an image of a baby sea otter, exuding cuteness. The small, furry creature should be floating blissfully on its back in clear, calm waters, its round button eyes are brimming with innocence and curiosity.]]]
        System.out.println(images);
        return path;
    }

    public void uploadImage(String path) {
        String image_id = "68627b45-78cb-4053-8a9a-dfb505219aef";
        RestClient restClient = RestClient.create();

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        parts.add("image", new FileSystemResource(path));


        ResponseEntity<Void> response = restClient.put()
                .uri("http://127.0.0.1:8000/api/ImageFile/{id}/", image_id)
                .body(parts)
                .contentType(MediaType.APPLICATION_JSON)
                .retrieve()
                .toBodilessEntity();

        System.out.println(response);

    }

    static class RequestResponseLoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            logRequestDetails(request, body, execution);
            return execution.execute(request, body);
        }

        private void logRequestDetails(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) {
            System.out.println("URI: " + request.getURI());
            System.out.println("Method: " + request.getMethod());
            System.out.println("Headers: " + request.getHeaders());
            System.out.println("Body: " + body); // Print the request body as a string;
        }
    }

    public void uploadImage2(String path, String imageId) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(new RequestResponseLoggingInterceptor()));

        String url = "http://127.0.0.1:8000/api/ImageFile/{iamgeId}/";
        //String id = "68627b45-78cb-4053-8a9a-dfb505219aef"; // Set the resource ID here

        File file = new File(path);

        if (file.exists() && file.canRead() && file.length() > 0) {
            System.out.println("File exists, is readable, and not empty.");
            System.out.println("File path: " + file.getAbsolutePath());
            System.out.println("File size: " + file.length() + " bytes");
            FileSystemResource fileResource = new FileSystemResource(file);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // Body
            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image", fileResource);

            // Wrap in HttpEntity
            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            // Make the PUT request
            restTemplate.put(url, requestEntity, imageId);
        }
        else {
            if (!file.exists()) {
                System.err.println("File does not exist: " + path);
            } else if (!file.canRead()) {
                System.err.println("File is not readable: " + path);
            } else if (file.length() == 0) {
                System.err.println("File is empty: " + path);
            }
        }
    }

    public void downloadFile(String fileUrl, String fileName) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Make an HTTP GET request to get the file as a resource
        ResponseEntity<Resource> response = restTemplate.getForEntity(fileUrl, Resource.class);

        // Check if the response is OK
        if (response.getStatusCode().is2xxSuccessful()) {
            // Read the file as ByteArrayResource
            ByteArrayResource resource = (ByteArrayResource) response.getBody();

            // Write the file to local disk
            if (resource != null) {
                Files.write(Path.of(fileName), resource.getByteArray(), StandardOpenOption.CREATE);
                System.out.println("File downloaded successfully: " + fileName);
            }
        } else {
            System.out.println("Failed to download file. Status code: " + response.getStatusCode());
        }
    }
}

