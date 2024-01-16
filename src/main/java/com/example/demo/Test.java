package com.example.demo;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;


public class Test {


    public static void uploadFile(String requestURL, File file) throws IOException {
        String boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("PUT");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = httpURLConnection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

        // Add file part
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + file.getName() + "\"\r\n");
        writer.append("Content-Type: " + HttpURLConnection.guessContentTypeFromName(file.getName()) + "\r\n");
        writer.append("Content-Transfer-Encoding: binary\r\n");
        writer.append("\r\n");
        writer.flush();

        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();

        writer.append("\r\n").flush();
        writer.append("--").append(boundary).append("--").append("\r\n");
        writer.close();

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read response
            InputStream responseStream = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = responseStreamReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            responseStreamReader.close();
            String response = stringBuilder.toString();
            System.out.println("Response: " + response);
        } else {
            System.out.println("Server returned non-OK status: " + responseCode);
        }

        httpURLConnection.disconnect();
    }

    public static void main(String[] args) {
        try {
            File file = new File("/Users/rastoshi/workplace/audio/audio-processor/demo/src/main/java/test_file.png"); // Replace with your file path
            String requestURL = "http://127.0.0.1:8000/api/ImageFile/68627b45-78cb-4053-8a9a-dfb505219aef/"; // Replace with your URL and Resource ID
            uploadFile(requestURL, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

