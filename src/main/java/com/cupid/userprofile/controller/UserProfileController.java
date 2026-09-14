package com.cupid.userprofile.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cupid.userprofile.model.UserProfile;
import com.cupid.userprofile.service.UserProfileService;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("userProfile", new UserProfile());
        return "userprofile/create";
    }

    @PostMapping("/create")
    public String createProfile(
            @ModelAttribute("userProfile") UserProfile profile,
            Model model) {

        try {
            userProfileService.createProfile(profile);
            return "redirect:/profile/" + profile.getUserId();

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "userprofile/create";
        }
    }

    @GetMapping("/{userId}")
    public String getProfile(
            @PathVariable int userId,
            Model model) {

        Optional<UserProfile> profile =
                userProfileService.fetchProfile(userId);

        if (profile.isEmpty()) {
            return "userprofile/not-found";
        }

        model.addAttribute("userProfile", profile.get());

        return "userprofile/profile";
    }

    @GetMapping("/{userId}/edit")
    public String showEditForm(
            @PathVariable int userId,
            Model model) {

        Optional<UserProfile> profile =
                userProfileService.fetchProfile(userId);

        if (profile.isEmpty()) {
            return "userprofile/not-found";
        }

        model.addAttribute("userProfile", profile.get());

        return "userprofile/edit";
    }

    @PostMapping("/{userId}/edit")
    public String updateProfile(
            @PathVariable int userId,
            @ModelAttribute("userProfile") UserProfile profile,
            Model model) {

        profile.setUserId(userId);

        try {
            boolean updated =
                    userProfileService.updateProfile(profile);

            if (!updated) {
                return "userprofile/not-found";
            }

            return "redirect:/profile/" + userId;

        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "userprofile/edit";
        }
    }

    @PostMapping("/{userId}/delete")
    public String deleteProfile(
            @PathVariable int userId,
            Model model) {

        try {
            boolean deleted =
                    userProfileService.deleteProfile(userId);

            if (!deleted) {
                return "userprofile/not-found";
            }

            return "userprofile/deleted";

        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "userprofile/profile";
        }
    }
}