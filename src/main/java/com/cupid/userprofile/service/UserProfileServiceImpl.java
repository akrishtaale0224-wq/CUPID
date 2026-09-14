package com.cupid.userprofile.service;


import java.util.Optional;

import com.cupid.userprofile.config.ApplicationSettings;
import com.cupid.userprofile.dao.UserProfileDAO;
import com.cupid.userprofile.dao.UserProfileDAOImpl;
import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.util.ProfilePictureValidator;



public class UserProfileServiceImpl implements UserProfileService {


    private final UserProfileDAO userProfileDAO;



    public UserProfileServiceImpl() {

        this.userProfileDAO =
                new UserProfileDAOImpl();

    }



    @Override
    public boolean createProfile(UserProfile profile) {

        validateProfile(profile);

        profile.setName(
                sanitiseInput(profile.getName())
        );

        profile.setBio(
                sanitiseInput(profile.getBio())
        );


        validateProfilePicture(profile);


        return userProfileDAO.createProfile(profile);

    }




    @Override
    public boolean updateProfile(UserProfile profile) {

        validateProfile(profile);


        profile.setName(
                sanitiseInput(profile.getName())
        );


        profile.setBio(
                sanitiseInput(profile.getBio())
        );


        validateProfilePicture(profile);


        return userProfileDAO.updateProfile(profile);

    }





    @Override
    public Optional<UserProfile> fetchProfile(int userId) {


        if (userId <= 0) {

            throw new IllegalArgumentException(
                    "Invalid user ID"
            );

        }


        return userProfileDAO.getProfileById(userId);

    }




    @Override
    public boolean deleteProfile(int userId) {


        if (!ApplicationSettings.ALLOW_ACCOUNT_DELETION) {

            throw new IllegalStateException(
                    "Account deletion disabled"
            );

        }


        return userProfileDAO.deleteProfile(userId);

    }





    private void validateProfile(UserProfile profile) {


        if (profile == null) {

            throw new IllegalArgumentException(
                    "Profile cannot be null"
            );

        }


        if (profile.getName() == null ||
                profile.getName().isBlank()) {


            throw new IllegalArgumentException(
                    "Name cannot be empty"
            );

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


        if (!ProfilePictureValidator
                .isValidImage(profile.getProfilePicture())) {


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
