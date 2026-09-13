package com.cupid.messaging.service;

import com.cupid.messaging.model.Message;
import com.cupid.messaging.repository.MessageRepository;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessagingTimeoutTest {

    @Test
    void failsGracefullyWhenPersistenceTakesTooLong() {

        MessageRepository messageRepository =
                Mockito.mock(MessageRepository.class);

        MessageSanitizer messageSanitizer =
                new MessageSanitizer();

        MessagingService messagingService =
                new MessagingService(
                        messageRepository,
                        messageSanitizer
                );

        when(messageRepository.save(any(Message.class)))
                .thenAnswer(invocation -> {
                    Thread.sleep(6000);
                    return invocation.getArgument(0);
                });

        assertThrows(
                MessagingException.class,
                () -> messagingService.sendMessage(
                        "Safwan",
                        "Alex",
                        "Hello"
                )
        );
    }
}
