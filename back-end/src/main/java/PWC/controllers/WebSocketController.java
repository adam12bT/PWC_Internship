package PWC.controllers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/websocket-message")
    @SendTo("/topic/websocket-messages")
    public String processMessageFromClient(String message) {
        return message;
    }
}
