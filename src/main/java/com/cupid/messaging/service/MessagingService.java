package com.cupid.messaging.service;

import com.cupid.messaging.model.Message;
import com.cupid.messaging.repository.MessageRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@Service
public class MessagingService {

    private final MessageRepository messageRepository;
    private final MessageSanitizer messageSanitizer;

    @Value("${messaging.encouragement.enabled:true}")
    private boolean encouragementEnabled;

    @Value("${messaging.persistence.timeout.seconds:5}")
    private long persistenceTimeoutSeconds = 5;

    public MessagingService(
            MessageRepository messageRepository,
            MessageSanitizer messageSanitizer) {

        this.messageRepository = messageRepository;
        this.messageSanitizer = messageSanitizer;
    }

    public Message sendMessage(String sender, String receiver, String content) {

        String cleanSender = messageSanitizer.sanitise(sender);
        String cleanReceiver = messageSanitizer.sanitise(receiver);
        String cleanContent = messageSanitizer.sanitise(content);

        Message message = new Message(
                cleanSender,
                cleanReceiver,
                cleanContent,
                LocalDateTime.now()
        );

        return executeWithTimeout(
                () -> messageRepository.save(message),
                "Unable to save message."
        );
    }

    public List<Message> getAllMessages() {

        return executeWithTimeout(
                () -> messageRepository.findAll(),
                "Unable to retrieve messages."
        );
    }

    public List<Message> getConversation(String user1, String user2) {

        return executeWithTimeout(
                () -> messageRepository
                        .findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
                                user1,
                                user2,
                                user2,
                                user1
                        ),
                "Unable to retrieve conversation history."
        );
    }

    public List<String> getThreads(String username) {

        List<Message> messages = executeWithTimeout(
                () -> messageRepository
                        .findBySenderOrReceiverOrderBySentAtDesc(
                                username,
                                username
                        ),
                "Unable to retrieve message threads."
        );

        Set<String> threadUsers = new LinkedHashSet<>();

        for (Message message : messages) {

            if (username.equals(message.getSender())) {
                threadUsers.add(message.getReceiver());
            } else {
                threadUsers.add(message.getSender());
            }
        }

        return new ArrayList<>(threadUsers);
    }

    public boolean isEncouragementEnabled() {
        return encouragementEnabled;
    }

    public String getEncouragementMessage() {

        if (encouragementEnabled) {
            return "You have matches waiting. Start a conversation!";
        }

        return "";
    }

    private <T> T executeWithTimeout(
            Supplier<T> persistenceOperation,
            String errorMessage) {

        try {

            return CompletableFuture
                    .supplyAsync(persistenceOperation)
                    .orTimeout(
                            persistenceTimeoutSeconds,
                            TimeUnit.SECONDS
                    )
                    .join();

        } catch (CompletionException ex) {

            Throwable cause = ex.getCause();

            if (cause instanceof TimeoutException) {
                throw new MessagingException(
                        "Persistence operation exceeded "
                        + persistenceTimeoutSeconds
                        + " seconds. Please try again later.",
                        cause
                );
            }

            if (cause instanceof DataAccessException) {
                throw new MessagingException(
                        errorMessage + " Please try again later.",
                        cause
                );
            }

            throw new MessagingException(
                    errorMessage + " Please try again later.",
                    cause
            );
        }
    }
}