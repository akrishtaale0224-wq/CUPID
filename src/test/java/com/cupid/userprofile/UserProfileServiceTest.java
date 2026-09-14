package com.cupid.userprofile;

import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.repository.UserProfileRepository;
import com.cupid.userprofile.service.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserProfileServiceTest {

    private UserProfileRepository repository;
    private UserProfileServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(UserProfileRepository.class);
        service = new UserProfileServiceImpl(repository);
    }

    @Test
    void createProfileShouldSaveValidProfile() {

        UserProfile profile =
                new UserProfile("Akrishta", 25, "Hello", "photo.jpg");

        when(repository.save(profile)).thenReturn(profile);

        boolean result = service.createProfile(profile);

        assertTrue(result);
        verify(repository).save(profile);
    }

    @Test
    void createProfileShouldRejectUnderageUser() {

        UserProfile profile =
                new UserProfile("Akrishta", 17, "Hello", "photo.jpg");

        assertThrows(
                IllegalArgumentException.class,
                () -> service.createProfile(profile)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void createProfileShouldSanitiseInput() {

        UserProfile profile =
                new UserProfile(
                        "<Akrishta>",
                        25,
                        "<Hello>",
                        "photo.jpg"
                );

        when(repository.save(profile)).thenReturn(profile);

        service.createProfile(profile);

        assertEquals("Akrishta", profile.getName());
        assertEquals("Hello", profile.getBio());
        verify(repository).save(profile);
    }

    @Test
    void fetchProfileShouldReturnProfile() {

        UserProfile profile =
                new UserProfile("Akrishta", 25, "Hello", "photo.jpg");

        profile.setUserId(1);

        when(repository.findById(1))
                .thenReturn(Optional.of(profile));

        Optional<UserProfile> result =
                service.fetchProfile(1);

        assertTrue(result.isPresent());
        assertEquals("Akrishta", result.get().getName());
    }

    @Test
    void fetchProfileShouldRejectInvalidId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> service.fetchProfile(0)
        );

        verify(repository, never()).findById(any());
    }

    @Test
    void updateProfileShouldUpdateExistingProfile() {

        UserProfile profile =
                new UserProfile("Akrishta", 25, "Updated", "photo.jpg");

        profile.setUserId(1);

        when(repository.existsById(1)).thenReturn(true);
        when(repository.save(profile)).thenReturn(profile);

        boolean result = service.updateProfile(profile);

        assertTrue(result);
        verify(repository).save(profile);
    }

    @Test
    void updateProfileShouldReturnFalseForMissingProfile() {

        UserProfile profile =
                new UserProfile("Akrishta", 25, "Updated", "photo.jpg");

        profile.setUserId(1);

        when(repository.existsById(1)).thenReturn(false);

        boolean result = service.updateProfile(profile);

        assertFalse(result);
        verify(repository, never()).save(any());
    }

    @Test
    void deleteProfileShouldDeleteExistingProfile() {

        when(repository.existsById(1)).thenReturn(true);

        boolean result = service.deleteProfile(1);

        assertTrue(result);
        verify(repository).deleteById(1);
    }

    @Test
    void deleteProfileShouldReturnFalseForMissingProfile() {

        when(repository.existsById(1)).thenReturn(false);

        boolean result = service.deleteProfile(1);

        assertFalse(result);
        verify(repository, never()).deleteById(any());
    }
}