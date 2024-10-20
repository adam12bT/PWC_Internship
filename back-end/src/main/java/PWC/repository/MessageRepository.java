package PWC.repository;// MessageRepository.java

import PWC.entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
}
