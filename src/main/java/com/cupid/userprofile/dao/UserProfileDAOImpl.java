package com.cupid.userprofile.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.util.DatabaseConnection;


public class UserProfileDAOImpl implements UserProfileDAO {


    @Override
    public boolean createProfile(UserProfile profile) {

        String sql =
                "INSERT INTO user_profile " +
                "(name, age, bio, profile_picture) " +
                "VALUES (?, ?, ?, ?)";


        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {


            statement.setString(1, profile.getName());
            statement.setInt(2, profile.getAge());
            statement.setString(3, profile.getBio());
            statement.setString(4, profile.getProfilePicture());


            return statement.executeUpdate() > 0;


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Unable to create user profile",
                    e
            );
        }
    }



    @Override
    public boolean updateProfile(UserProfile profile) {

        String sql =
                "UPDATE user_profile SET " +
                "name=?, age=?, bio=?, profile_picture=? " +
                "WHERE user_id=?";


        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {


            statement.setString(1, profile.getName());
            statement.setInt(2, profile.getAge());
            statement.setString(3, profile.getBio());
            statement.setString(4, profile.getProfilePicture());
            statement.setInt(5, profile.getUserId());


            return statement.executeUpdate() > 0;


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Unable to update user profile",
                    e
            );
        }
    }




    @Override
    public Optional<UserProfile> getProfileById(int userId) {


        String sql =
                "SELECT user_id, name, age, bio, profile_picture " +
                "FROM user_profile WHERE user_id=?";


        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {


            statement.setInt(1, userId);


            ResultSet result =
                    statement.executeQuery();


            if (result.next()) {


                UserProfile profile =
                        new UserProfile();


                profile.setUserId(
                        result.getInt("user_id")
                );


                profile.setName(
                        result.getString("name")
                );


                profile.setAge(
                        result.getInt("age")
                );


                profile.setBio(
                        result.getString("bio")
                );


                profile.setProfilePicture(
                        result.getString("profile_picture")
                );


                return Optional.of(profile);

            }


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Unable to fetch user profile",
                    e
            );
        }


        return Optional.empty();
    }




    @Override
    public boolean deleteProfile(int userId) {


        String sql =
                "DELETE FROM user_profile WHERE user_id=?";


        try (Connection connection =
                     DatabaseConnection.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement(sql)) {


            statement.setInt(1, userId);


            return statement.executeUpdate() > 0;


        } catch (SQLException e) {

            throw new RuntimeException(
                    "Unable to delete user profile",
                    e
            );
        }
    }

}
