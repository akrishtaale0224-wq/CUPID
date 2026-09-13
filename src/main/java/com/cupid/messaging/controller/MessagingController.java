package com.cupid.messaging.controller;

import com.cupid.messaging.service.MessagingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessagingController {

    private final MessagingService messagingService;

    public MessagingController(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    @GetMapping("/messages")
    public String showMessages(Model model) {

        model.addAttribute(
                "messages",
                messagingService.getAllMessages()
        );

        model.addAttribute(
                "encouragementEnabled",
                messagingService.isEncouragementEnabled()
        );

        model.addAttribute(
                "encouragementMessage",
                messagingService.getEncouragementMessage()
        );

        return "messaging/messages";
    }

    @PostMapping("/messages/send")
    public String sendMessage(
            @RequestParam String sender,
            @RequestParam String receiver,
            @RequestParam String content) {

        messagingService.sendMessage(sender, receiver, content);

        return "redirect:/messages";
    }

    @GetMapping("/messages/conversation")
    public String showConversation(
            @RequestParam String user1,
            @RequestParam String user2,
            Model model) {

        model.addAttribute(
                "conversation",
                messagingService.getConversation(user1, user2)
        );

        model.addAttribute("user1", user1);
        model.addAttribute("user2", user2);

        return "messaging/conversation";
    }

    @GetMapping("/messages/threads")
    public String showThreads(
            @RequestParam String user,
            Model model) {

        model.addAttribute("user", user);

        model.addAttribute(
                "threads",
                messagingService.getThreads(user)
        );

        return "messaging/threads";
    }
}