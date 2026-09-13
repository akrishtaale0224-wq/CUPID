package com.cupid.messaging.service;

import com.cupid.messaging.model.Message;
import com.cupid.messaging.repository.MessageRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessagingServiceTest {

    private MessageRepository messageRepository;
    private MessageSanitizer messageSanitizer;
    private MessagingService messagingService;

    @BeforeEach
    void setUp() {

        messageRepository = Mockito.mock(MessageRepository.class);
        messageSanitizer = new MessageSanitizer();

        messagingService = new MessagingService(
                messageRepository,
                messageSanitizer
        );
    }

    @Test
    void sendsAndSanitisesMessage() {

        when(messageRepository.save(any(Message.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Message result = messagingService.sendMessage(
                "Safwan",
                "Alex",
                "<b>Hello</b>"
        );

        assertEquals("Safwan", result.getSender());
        assertEquals("Alex", result.getReceiver());
        assertEquals("&lt;b&gt;Hello&lt;/b&gt;", result.getContent());
    }

    @Test
    void retrievesConversationHistory() {

        Message message = new Message();
        message.setSender("Safwan");
        message.setReceiver("Alex");
        message.setContent("Hello");

        when(messageRepository
                .findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
                        "Safwan",
                        "Alex",
                        "Alex",
                        "Safwan"
                ))
                .thenReturn(List.of(message));

        List<Message> result =
                messagingService.getConversation("Safwan", "Alex");

        assertEquals(1, result.size());
        assertEquals("Hello", result.get(0).getContent());
    }
}