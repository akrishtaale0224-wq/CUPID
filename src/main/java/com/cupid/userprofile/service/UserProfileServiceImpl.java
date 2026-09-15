package com.cupid.userprofile.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cupid.userprofile.config.ApplicationSettings;
import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.repository.UserProfileRepository;
import com.cupid.userprofile.util.ProfilePictureValidator;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public boolean createProfile(UserProfile profile) {
        validateProfile(profile);

        profile.setName(sanitiseInput(profile.getName()));
        profile.setBio(sanitiseInput(profile.getBio()));

        validateProfilePicture(profile);

        userProfileRepository.save(profile);
        return true;
    }

    @Override
    public boolean updateProfile(UserProfile profile) {
        validateProfile(profile);

        profile.setName(sanitiseInput(profile.getName()));
        profile.setBio(sanitiseInput(profile.getBio()));

        validateProfilePicture(profile);

        if (profile.getUserId() == null ||
                !userProfileRepository.existsById(profile.getUserId())) {
            return false;
        }

        userProfileRepository.save(profile);
        return true;
    }

    @Override
    public Optional<UserProfile> fetchProfile(int userId) {

        if (userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        return userProfileRepository.findById(userId);
    }

    @Override
    public boolean deleteProfile(int userId) {

        if (!ApplicationSettings.ALLOW_ACCOUNT_DELETION) {
            throw new IllegalStateException("Account deletion disabled");
        }

        if (!userProfileRepository.existsById(userId)) {
            return false;
        }

        userProfileRepository.deleteById(userId);
        return true;
    }

    private void validateProfile(UserProfile profile) {

        if (profile == null) {
            throw new IllegalArgumentException("Profile cannot be null");
        }

        if (profile.getName() == null ||
                profile.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (profile.getAge() < 18) {
            throw new IllegalArgumentException(
                    "User must be 18 or older"
            );
        }
    }

    private void validateProfilePicture(UserProfile profile) {

        if (profile.getProfilePicture() == null ||
                profile.getProfilePicture().isBlank()) {
            return;
        }

        if (!ProfilePictureValidator.isValidImage(
                profile.getProfilePicture())) {

            throw new IllegalArgumentException(
                    "Invalid profile picture format"
            );
        }
    }

    private String sanitiseInput(String input) {

        if (input == null) {
            return "";
        }

        return input
                .replace("<", "")
                .replace(">", "")
                .trim();
    }
}