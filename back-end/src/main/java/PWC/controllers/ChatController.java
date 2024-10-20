package PWC.controllers;

import PWC.entities.ChatMessage;
import PWC.repository.MessageRepository;
import PWC.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChatController {

    private final ChatService chatService;
    private final MessageRepository messageRepository;

    public ChatController(ChatService chatService, MessageRepository messageRepository) {
        this.chatService = chatService;
        this.messageRepository = messageRepository;
    }

    @MessageMapping("/message")
    public void sendMessage(@Payload ChatMessage message) {
        System.out.println(message);
        chatService.sendMessage(message);
        System.out.println(message);
    }

    @MessageMapping("/chat-message")
    @SendTo("/topic/chat-messages")
    public ChatMessage handleChatMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
        chatService.sendMessage(message);
        return message;
    }

    @MessageMapping("/fetch-messages")
    @SendTo("/topic/messages")
    public List<ChatMessage> fetchMessages() {
        return messageRepository.findAll();
    }

    @GetMapping("/api/messages")
    @ResponseBody
    public List<ChatMessage> getAllMessages() {
        return messageRepository.findAll();
    }
}
