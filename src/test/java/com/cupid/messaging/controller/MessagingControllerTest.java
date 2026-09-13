package com.cupid.messaging.controller;

import com.cupid.messaging.model.Message;
import com.cupid.messaging.service.MessagingService;

import org.junit.jupiter.api.Test;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class MessagingControllerTest {

    @Test
    void displaysMessagesPage() throws Exception {

        MessagingService messagingService =
                mock(MessagingService.class);

        Message message = new Message(
                "Safwan",
                "Alex",
                "Hello",
                LocalDateTime.now()
        );

        when(messagingService.getAllMessages())
                .thenReturn(List.of(message));

        when(messagingService.isEncouragementEnabled())
                .thenReturn(true);

        when(messagingService.getEncouragementMessage())
                .thenReturn(
                        "You have matches waiting. Start a conversation!"
                );

        MessagingController controller =
                new MessagingController(messagingService);

        MockMvc mockMvc =
                standaloneSetup(controller).build();

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(view().name("messaging/messages"))
                .andExpect(model().attributeExists("messages"))
                .andExpect(model().attributeExists("encouragementEnabled"))
                .andExpect(model().attributeExists("encouragementMessage"));
    }

    @Test
    void displaysConversationHistory() throws Exception {

        MessagingService messagingService =
                mock(MessagingService.class);

        Message message = new Message(
                "Safwan",
                "Alex",
                "Hello",
                LocalDateTime.now()
        );

        when(messagingService.getConversation(
                "Safwan",
                "Alex"
        )).thenReturn(List.of(message));

        MessagingController controller =
                new MessagingController(messagingService);

        MockMvc mockMvc =
                standaloneSetup(controller).build();

        mockMvc.perform(
                get("/messages/conversation")
                        .param("user1", "Safwan")
                        .param("user2", "Alex")
        )
                .andExpect(status().isOk())
                .andExpect(view().name("messaging/conversation"))
                .andExpect(model().attributeExists("conversation"))
                .andExpect(model().attribute("user1", "Safwan"))
                .andExpect(model().attribute("user2", "Alex"));
    }
}