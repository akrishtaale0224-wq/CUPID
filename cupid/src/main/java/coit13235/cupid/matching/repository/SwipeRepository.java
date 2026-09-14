/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ASUS
 */
package com.cupid.matching.repository;

import com.cupid.matching.entity.Swipe;
import com.cupid.matching.entity.SwipeDecision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SwipeRepository extends JpaRepository<Swipe, Long> {

    Optional<Swipe> findBySwiperIdAndTargetUserId(
            Long swiperId,
            Long targetUserId
    );

    List<Swipe> findBySwiperIdOrderByCreatedAtDesc(
            Long swiperId
    );

    boolean existsBySwiperIdAndTargetUserIdAndDecision(
            Long swiperId,
            Long targetUserId,
            SwipeDecision decision
    );
}
