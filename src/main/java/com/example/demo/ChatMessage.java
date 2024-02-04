package com.example.demo;

import java.io.Serializable;

public record ChatMessage(
        String id,
        String chat_id,
        String audio_message_id,
        String image_id,
        String timestamp
) implements Serializable {
  // If you need to add logic like validation or additional methods, you can add them here.
}



