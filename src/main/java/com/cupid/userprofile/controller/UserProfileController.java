package com.cupid.userprofile.controller;


import java.util.Optional;

import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.service.UserProfileService;
import com.cupid.userprofile.service.UserProfileServiceImpl;


public class UserProfileController {


    private final UserProfileService userProfileService;


    public UserProfileController() {

        this.userProfileService =
                new UserProfileServiceImpl();

    }



    public boolean createProfile(UserProfile profile) {

        return userProfileService.createProfile(profile);

    }



    public boolean updateProfile(UserProfile profile) {

        return userProfileService.updateProfile(profile);

    }



    public Optional<UserProfile> getProfile(int userId) {

        return userProfileService.fetchProfile(userId);

    }



    public boolean deleteProfile(int userId) {

        return userProfileService.deleteProfile(userId);

    }

}
