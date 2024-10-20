package PWC.services;

import PWC.entities.ChatMessage;
import PWC.repository.MessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageRepository messageRepository;

    public ChatService(SimpMessagingTemplate messagingTemplate, MessageRepository messageRepository) {
        this.messagingTemplate = messagingTemplate;
        this.messageRepository = messageRepository;
    }

    public void sendMessage(ChatMessage message) {
        messagingTemplate.convertAndSend("/topic/messages", message);
        System.out.println("Message sent to /topic/messages");
        saveMessage(message); // Save the message to the database
    }

    @Transactional // Ensure transactional management for database operations
    public void saveMessage(ChatMessage message) {
        messageRepository.save(message);
        System.out.println("Message ");


    }
}
