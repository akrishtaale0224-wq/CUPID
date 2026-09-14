package com.cupid.userprofile;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.service.UserProfileService;
import com.cupid.userprofile.service.UserProfileServiceImpl;


class UserProfileServiceTest {


    private final UserProfileService service =
            new UserProfileServiceImpl();



    @Test
    void testCreateValidProfile() {


        UserProfile profile =
                new UserProfile(
                        0,
                        "John",
                        25,
                        "Software developer",
                        "john.jpg"
                );


        boolean result =
                service.createProfile(profile);


        assertTrue(result);

    }





    @Test
    void testRejectUnderAgeUser() {


        UserProfile profile =
                new UserProfile(
                        0,
                        "Child User",
                        15,
                        "Invalid age",
                        "child.jpg"
                );


        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.createProfile(profile)
                );


        assertNotNull(exception);

        assertEquals(
                "User must be 18 or older",
                exception.getMessage()
        );

    }





    @Test
    void testRejectInvalidImageFormat() {


        UserProfile profile =
                new UserProfile(
                        0,
                        "Test User",
                        25,
                        "Testing image",
                        "virus.exe"
                );


        IllegalArgumentException exception =
                assertThrows(
                        IllegalArgumentException.class,
                        () -> service.createProfile(profile)
                );


        assertNotNull(exception);

        assertEquals(
                "Invalid profile picture format",
                exception.getMessage()
        );

    }





    @Test
    void testInputSanitisation() {


        UserProfile profile =
                new UserProfile(
                        0,
                        "<script>Test</script>",
                        25,
                        "<h1>Hello</h1>",
                        "test.png"
                );


        boolean result =
                service.createProfile(profile);


        assertTrue(result);

    }

}
