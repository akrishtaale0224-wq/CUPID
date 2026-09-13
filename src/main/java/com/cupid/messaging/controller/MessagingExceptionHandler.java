package com.cupid.messaging.controller;

import com.cupid.messaging.service.MessagingException;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MessagingExceptionHandler {

    @ExceptionHandler(MessagingException.class)
    public String handleMessagingException(
            MessagingException exception,
            Model model) {

        model.addAttribute(
                "errorMessage",
                exception.getMessage()
        );

        return "messaging/error";
    }
}