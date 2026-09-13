package com.cupid.messaging.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageSanitizerTest {

    @Test
    void sanitisesUnsafeHtmlCharacters() {

        MessageSanitizer sanitizer = new MessageSanitizer();

        String result = sanitizer.sanitise(
                "<script>alert('hello')</script>"
        );

        assertEquals(
                "&lt;script&gt;alert(&#39;hello&#39;)&lt;/script&gt;",
                result
        );
    }

    @Test
    void trimsWhitespace() {

        MessageSanitizer sanitizer = new MessageSanitizer();

        String result = sanitizer.sanitise("   Hello   ");

        assertEquals("Hello", result);
    }

    @Test
    void convertsNullToEmptyString() {

        MessageSanitizer sanitizer = new MessageSanitizer();

        String result = sanitizer.sanitise(null);

        assertEquals("", result);
    }
}