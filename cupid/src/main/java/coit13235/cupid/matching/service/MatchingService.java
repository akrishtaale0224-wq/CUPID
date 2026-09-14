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

import java.util.List;

public interface MatchingService {

    boolean swipe(
            Long currentUserId,
            Long targetUserId,
            SwipeDecision decision
    );

    List<Swipe> getSwipeHistory(Long userId);

    List<Match> getMatches(Long userId);

    boolean isSwipeMoreEthicsEnabled();
}
