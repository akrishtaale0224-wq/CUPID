package com.cupid.messaging.repository;

import com.cupid.messaging.model.Message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    void savesAndRetrievesConversationHistory() {

        Message first = new Message(
                "Safwan",
                "Alex",
                "Hello Alex",
                LocalDateTime.now()
        );

        Message second = new Message(
                "Alex",
                "Safwan",
                "Hi Safwan",
                LocalDateTime.now().plusSeconds(1)
        );

        messageRepository.save(first);
        messageRepository.save(second);

        List<Message> conversation =
                messageRepository
                        .findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
                                "Safwan",
                                "Alex",
                                "Alex",
                                "Safwan"
                        );

        assertEquals(2, conversation.size());
        assertEquals("Hello Alex", conversation.get(0).getContent());
        assertEquals("Hi Safwan", conversation.get(1).getContent());
    }

    @Test
    void retrievesMessagesForThreadList() {

        messageRepository.save(
                new Message(
                        "Safwan",
                        "Alex",
                        "Hello",
                        LocalDateTime.now()
                )
        );

        messageRepository.save(
                new Message(
                        "John",
                        "Safwan",
                        "Hi",
                        LocalDateTime.now().plusSeconds(1)
                )
        );

        List<Message> messages =
                messageRepository
                        .findBySenderOrReceiverOrderBySentAtDesc(
                                "Safwan",
                                "Safwan"
                        );

        assertEquals(2, messages.size());
    }
}