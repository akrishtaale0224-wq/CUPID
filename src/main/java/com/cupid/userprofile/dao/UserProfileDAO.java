package com.cupid.userprofile.dao;

import java.util.Optional;

import com.cupid.userprofile.model.UserProfile;

public interface UserProfileDAO {

    // Create a new user profile
    boolean createProfile(UserProfile profile);


    // Update existing profile details
    boolean updateProfile(UserProfile profile);


    // Fetch profile by user ID
    Optional<UserProfile> getProfileById(int userId);


    // Delete user profile
    boolean deleteProfile(int userId);

}
