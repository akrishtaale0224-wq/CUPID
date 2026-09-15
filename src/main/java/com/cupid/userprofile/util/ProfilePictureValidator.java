package com.cupid.userprofile.util;


public class ProfilePictureValidator {


    private ProfilePictureValidator() {

    }



    public static boolean isValidImage(String fileName) {


        if (fileName == null ||
                fileName.isBlank()) {

            return false;

        }


        String lowerCaseName =
                fileName.toLowerCase();


        return lowerCaseName.endsWith(".jpg")
                || lowerCaseName.endsWith(".jpeg")
                || lowerCaseName.endsWith(".png");

    }

}
