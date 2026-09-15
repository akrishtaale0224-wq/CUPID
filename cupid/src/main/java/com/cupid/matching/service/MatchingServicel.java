/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
package com.cupid.matching.service;

import com.cupid.matching.entity.Match;
import com.cupid.matching.entity.Swipe;
import com.cupid.matching.entity.SwipeDecision;
import com.cupid.matching.exception.MatchingException;
import com.cupid.matching.repository.MatchRepository;
import com.cupid.matching.repository.SwipeRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchingServicel implements MatchingService {

    private final SwipeRepository swipeRepository;
    private final MatchRepository matchRepository;

    @Value("${cupid.ethics.swipe-more:false}")
    private boolean swipeMoreEthicsEnabled;

    public MatchingServicel(
            SwipeRepository swipeRepository,
            MatchRepository matchRepository) {

        this.swipeRepository = swipeRepository;
        this.matchRepository = matchRepository;
    }

    @Override
    @Transactional
    public boolean swipe(
            Long currentUserId,
            Long targetUserId,
            SwipeDecision decision) {

        // Input validation
        if (currentUserId == null || targetUserId == null) {
            throw new MatchingException(
                    "User IDs cannot be null."
            );
        }

        if (currentUserId <= 0 || targetUserId <= 0) {
            throw new MatchingException(
                    "User IDs must be positive."
            );
        }

        if (currentUserId.equals(targetUserId)) {
            throw new MatchingException(
                    "A user cannot swipe on themselves."
            );
        }

        if (decision == null) {
            throw new MatchingException(
                    "Swipe decision is required."
            );
        }

        // Save or update swipe
        Swipe swipe = swipeRepository
                .findBySwiperIdAndTargetUserId(
                        currentUserId,
                        targetUserId
                )
                .orElse(new Swipe());

        swipe.setSwiperId(currentUserId);
        swipe.setTargetUserId(targetUserId);
        swipe.setDecision(decision);

        if (swipe.getCreatedAt() == null) {
            swipe.setCreatedAt(
                    java.time.LocalDateTime.now()
            );
        }

        swipeRepository.save(swipe);

        // Only LIKE can create a match
        if (decision == SwipeDecision.LIKE) {

            boolean mutualLike =
                    swipeRepository
                            .existsBySwiperIdAndTargetUserIdAndDecision(
                                    targetUserId,
                                    currentUserId,
                                    SwipeDecision.LIKE
                            );

            if (mutualLike) {

                createMatch(
                        currentUserId,
                        targetUserId
                );

                return true;
            }
        }

        return false;
    }

    private void createMatch(
            Long firstUser,
            Long secondUser) {

        Long userOne = Math.min(
                firstUser,
                secondUser
        );

        Long userTwo = Math.max(
                firstUser,
                secondUser
        );

        boolean alreadyExists =
                matchRepository
                        .existsByUserOneIdAndUserTwoId(
                                userOne,
                                userTwo
                        );

        if (!alreadyExists) {

            Match match = new Match(
                    userOne,
                    userTwo
            );

            matchRepository.save(match);
        }
    }

    @Override
    public List<Swipe> getSwipeHistory(Long userId) {

        if (userId == null || userId <= 0) {
            throw new MatchingException(
                    "Invalid user ID."
            );
        }

        return swipeRepository
                .findBySwiperIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Match> getMatches(Long userId) {

        if (userId == null || userId <= 0) {
            throw new MatchingException(
                    "Invalid user ID."
            );
        }

        return matchRepository
                .findByUserOneIdOrUserTwoId(
                        userId,
                        userId
                );
    }

    @Override
    public boolean isSwipeMoreEthicsEnabled() {
        return swipeMoreEthicsEnabled;
    }
}
