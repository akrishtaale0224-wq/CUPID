package com.cupid.messaging.service;

import org.springframework.stereotype.Component;

@Component
public class MessageSanitizer {

    public String sanitise(String input) {
        if (input == null) {
            return "";
        }

        return input
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .trim();
    }
}