package com.cupid.userprofile.model;

public class UserProfile {

    private int userId;
    private String name;
    private int age;
    private String bio;
    private String profilePicture;


    public UserProfile() {
    }


    public UserProfile(
            int userId,
            String name,
            int age,
            String bio,
            String profilePicture) {

        this.userId = userId;
        this.name = name;
        this.age = age;
        this.bio = bio;
        this.profilePicture = profilePicture;
    }


    public int getUserId() {
        return userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getAge() {
        return age;
    }


    public void setAge(int age) {
        this.age = age;
    }


    public String getBio() {
        return bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getProfilePicture() {
        return profilePicture;
    }


    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }


    @Override
    public String toString() {

        return "UserProfile{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", bio='" + bio + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                '}';
    }
}
