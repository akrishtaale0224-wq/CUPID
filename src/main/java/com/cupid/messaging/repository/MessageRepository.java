package com.cupid.messaging.repository;

import com.cupid.messaging.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findBySenderAndReceiverOrSenderAndReceiverOrderBySentAtAsc(
            String sender1,
            String receiver1,
            String sender2,
            String receiver2
    );

    List<Message> findBySenderOrReceiverOrderBySentAtDesc(
            String sender,
            String receiver
    );
}