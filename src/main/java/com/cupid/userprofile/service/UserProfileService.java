package com.cupid.userprofile.service;

import java.util.Optional;

import com.cupid.userprofile.model.UserProfile;

public interface UserProfileService {

    boolean createProfile(UserProfile profile);

    boolean updateProfile(UserProfile profile);

    Optional<UserProfile> fetchProfile(int userId);

    boolean deleteProfile(int userId);
}