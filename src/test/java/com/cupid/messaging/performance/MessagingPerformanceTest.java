package com.cupid.messaging.performance;

import com.cupid.messaging.model.Message;
import com.cupid.messaging.repository.MessageRepository;
import com.cupid.messaging.service.MessageSanitizer;
import com.cupid.messaging.service.MessagingService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MessagingPerformanceTest {

    @Test
    void supports100SimultaneousUsers() {

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
                .thenAnswer(invocation -> invocation.getArgument(0));

        List<CompletableFuture<Message>> requests =
                new ArrayList<>();

        long startTime = System.currentTimeMillis();

        for (int i = 1; i <= 100; i++) {

            final int userNumber = i;

            requests.add(
                    CompletableFuture.supplyAsync(() ->
                            messagingService.sendMessage(
                                    "User" + userNumber,
                                    "Receiver",
                                    "Hello from user " + userNumber
                            )
                    )
            );
        }

        CompletableFuture
                .allOf(
                        requests.toArray(
                                new CompletableFuture[0]
                        )
                )
                .join();

        long endTime = System.currentTimeMillis();

        long successfulRequests =
                requests.stream()
                        .filter(request -> !request.isCompletedExceptionally())
                        .count();

        long totalTime = endTime - startTime;

        System.out.println(
                "Successful requests: "
                + successfulRequests
        );

        System.out.println(
                "Total execution time: "
                + totalTime
                + " ms"
        );

        assertEquals(100, successfulRequests);
    }
}