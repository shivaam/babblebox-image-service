package com.example.demo;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SSLConfig {
    public void configureSSL() {
        // Trust all certificates
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        // Additional configuration to trust all SSL certificates can be added here
    }
}
