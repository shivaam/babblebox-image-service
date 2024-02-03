package com.example.demo;

import java.io.Serializable;
import org.apache.hc.core5.http.config.Lookup;

public record ChatMessage(
        String id,
        String chat_id,
        String audio_message_id,
        String image_id,
        String timestamp
) implements Serializable {
  // If you need to add logic like validation or additional methods, you can add them here.
}



