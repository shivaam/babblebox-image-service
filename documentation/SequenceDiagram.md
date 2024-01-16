sequenceDiagram
participant User
participant BabbleBoxServer as BabbleBox Server
participant PulsarNewChat as Apache Pulsar (new_chat_message_topic)
participant TranscriptionConsumer as Transcription Consumer
participant PulsarTranscribed as Apache Pulsar (chat_message_transcribed)
participant ImageGenerationConsumer as Image Generation Consumer

    User->>BabbleBoxServer: Records audio message (chat_message)
    BabbleBoxServer->>BabbleBoxServer: Saves the message associated with chat_id
    BabbleBoxServer->>PulsarNewChat: Sends chat_message_id
    PulsarNewChat->>TranscriptionConsumer: Message with chat_message_id
    TranscriptionConsumer->>BabbleBoxServer: Calls audioFile API for transcription using Whisper API
    TranscriptionConsumer->>PulsarTranscribed: Creates new message (chat_message_transcribed)
    PulsarTranscribed->>ImageGenerationConsumer: Reads transcribed message
    ImageGenerationConsumer->>BabbleBoxServer: Downloads transcription by calling AudioFile API
    ImageGenerationConsumer->>BabbleBoxServer: Uploads generated image linked to original chat message
