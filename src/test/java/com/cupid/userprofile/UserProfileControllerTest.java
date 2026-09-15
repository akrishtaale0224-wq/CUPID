package com.cupid.userprofile;

import com.cupid.userprofile.controller.UserProfileController;
import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.service.UserProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileControllerTest {

    private UserProfileService service;
    private UserProfileController controller;

    @BeforeEach
    void setUp() {
        service = mock(UserProfileService.class);
        controller = new UserProfileController(service);
    }

    @Test
    void createProfileShouldRedirectToProfile() {

        UserProfile profile =
                new UserProfile(
                        "Akrishta",
                        25,
                        "Hello",
                        "photo.jpg"
                );

        profile.setUserId(1);

        when(service.createProfile(profile))
                .thenReturn(true);

        MockMultipartFile picture =
                new MockMultipartFile(
                        "profilePicture",
                        "photo.jpg",
                        "image/jpeg",
                        "test image".getBytes()
                );

        Model model = new ExtendedModelMap();

        String result =
                controller.createProfile(
                        profile,
                        picture,
                        model
                );

        assertEquals(
                "redirect:/profile/1",
                result
        );

        verify(service).createProfile(profile);
    }

    @Test
    void getProfileShouldReturnProfilePage() {

        UserProfile profile =
                new UserProfile(
                        "Akrishta",
                        25,
                        "Hello",
                        "photo.jpg"
                );

        profile.setUserId(1);

        when(service.fetchProfile(1))
                .thenReturn(Optional.of(profile));

        Model model = new ExtendedModelMap();

        String result =
                controller.getProfile(
                        1,
                        model
                );

        assertEquals(
                "userprofile/profile",
                result
        );

        assertEquals(
                profile,
                model.getAttribute("userProfile")
        );
    }

    @Test
    void getProfileShouldReturnNotFound() {

        when(service.fetchProfile(999))
                .thenReturn(Optional.empty());

        Model model = new ExtendedModelMap();

        String result =
                controller.getProfile(
                        999,
                        model
                );

        assertEquals(
                "userprofile/not-found",
                result
        );
    }

    @Test
    void updateProfileShouldRedirectToProfile() {

        UserProfile profile =
                new UserProfile(
                        "Akrishta",
                        25,
                        "Updated",
                        "photo.jpg"
                );

        when(service.updateProfile(profile))
                .thenReturn(true);

        Model model = new ExtendedModelMap();

        String result =
                controller.updateProfile(
                        1,
                        profile,
                        model
                );

        assertEquals(
                "redirect:/profile/1",
                result
        );

        assertEquals(
                1,
                profile.getUserId()
        );

        verify(service).updateProfile(profile);
    }

    @Test
    void updateProfileShouldReturnNotFound() {

        UserProfile profile =
                new UserProfile(
                        "Akrishta",
                        25,
                        "Updated",
                        "photo.jpg"
                );

        when(service.updateProfile(profile))
                .thenReturn(false);

        Model model = new ExtendedModelMap();

        String result =
                controller.updateProfile(
                        999,
                        profile,
                        model
                );

        assertEquals(
                "userprofile/not-found",
                result
        );
    }

    @Test
    void deleteProfileShouldReturnDeletedPage() {

        when(service.deleteProfile(1))
                .thenReturn(true);

        Model model = new ExtendedModelMap();

        String result =
                controller.deleteProfile(
                        1,
                        model
                );

        assertEquals(
                "userprofile/deleted",
                result
        );

        verify(service).deleteProfile(1);
    }

    @Test
    void deleteProfileShouldReturnNotFound() {

        when(service.deleteProfile(999))
                .thenReturn(false);

        Model model = new ExtendedModelMap();

        String result =
                controller.deleteProfile(
                        999,
                        model
                );

        assertEquals(
                "userprofile/not-found",
                result
        );
    }
}

