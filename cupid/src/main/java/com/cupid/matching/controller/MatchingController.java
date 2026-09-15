/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */

package com.cupid.matching.controller;

import com.cupid.matching.entity.Swipe;
import com.cupid.matching.entity.Match;
import com.cupid.matching.entity.SwipeDecision;
import com.cupid.matching.service.MatchingService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/matching")
public class MatchingController {

    private final MatchingService matchingService;

    public MatchingController(MatchingService matchingService) {
        this.matchingService = matchingService;
    }

    /*
     * Displays the Discover page.
   
     */
    @GetMapping("/discover")
    public String discover(Model model) {

        Candidate candidate = new Candidate(
                2L,
                "Emma",
                25,
                "I love travelling, coffee and photography."
        );

        model.addAttribute("candidate", candidate);

        return "discover";
    }

    /*
     * Processes LIKE or DISLIKE.
     */
    @PostMapping("/swipe")
    public String swipe(
            @RequestParam Long currentUserId,
            @RequestParam Long targetUserId,
            @RequestParam SwipeDecision decision,
            RedirectAttributes redirectAttributes) {

        boolean matched = matchingService.swipe(
                currentUserId,
                targetUserId,
                decision
        );

        if (matched) {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "️ It's a Match!"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "message",
                    "Swipe saved successfully."
            );
        }

        return "redirect:/matching/discover";
    }

    /*
     * Displays swipe history.
     */
    @GetMapping("/history/{userId}")
    public String history(
            @PathVariable Long userId,
            Model model) {

        List<Swipe> swipes =
                matchingService.getSwipeHistory(userId);

        model.addAttribute("swipes", swipes);

        return "history";
    }

    /*
     * Displays matches.
     */
    @GetMapping("/matches/{userId}")
    public String matches(
            @PathVariable Long userId,
            Model model) {

        List<Match> matches =
                matchingService.getMatches(userId);

        model.addAttribute("matches", matches);

        return "matches";
    }

    /*
     * Simple candidate class for Week 9 demonstration.
     */
    public static class Candidate {

        private Long id;
        private String name;
        private int age;
        private String bio;

        public Candidate(
                Long id,
                String name,
                int age,
                String bio) {

            this.id = id;
            this.name = name;
            this.age = age;
            this.bio = bio;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public String getBio() {
            return bio;
        }
    }
}


