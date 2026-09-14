package com.cupid.userprofile;


import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cupid.userprofile.controller.UserProfileController;
import com.cupid.userprofile.model.UserProfile;



class UserProfileControllerTest {



    @Test
    void testCreateProfileThroughController() {


        UserProfile profile =
                new UserProfile(
                        0,
                        "Akrishta",
                        21,
                        "Software student",
                        "profile.jpg"
                );



        UserProfileController controller =
                new UserProfileController();



        boolean result =
                controller.createProfile(profile);



        assertTrue(result);

    }

}
